package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import co.touchlab.kermit.Logger
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.SyncResourceType
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper.asNetworkModels
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper.toDbModel
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper.toSyncParams
import io.github.reactivecircus.kstreamlined.kmp.networking.FeedService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.datetime.Clock
import kotlin.coroutines.cancellation.CancellationException

public class FeedSyncEngineImpl(
    private val feedService: FeedService,
    private val db: KStreamlinedDatabase,
    syncEngineScope: CoroutineScope,
    syncEngineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val clock: Clock = Clock.System,
) : FeedSyncEngine {
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Initializing)
    override val syncState: StateFlow<SyncState> = _syncState

    private val manualSyncTrigger = Channel<SyncRequest>()

    private val automaticSyncTrigger = db.feedOriginEntityQueries
        .allFeedOrigins().asFlow().mapToList(syncEngineDispatcher)
        .map { SyncRequest(forceRefresh = false, skipFeedSources = it.isNotEmpty()) }

    private val syncRequestEvaluator = SyncRequestEvaluator(
        syncConfig = SyncConfig.Default,
        feedOriginEntityQueries = db.feedOriginEntityQueries,
        lastSyncMetadataQueries = db.lastSyncMetadataQueries,
        clock = clock,
    )

    init {
        merge(manualSyncTrigger.receiveAsFlow(), automaticSyncTrigger)
            .onEach { syncRequest ->
                runCatching {
                    val decision = syncRequestEvaluator.evaluate(syncRequest)
                    if (decision.shouldSyncFeedSources || decision.shouldSyncFeedItems) {
                        _syncState.value = SyncState.Syncing
                        performSync(decision)
                    }
                    _syncState.value = SyncState.Idle
                }.onFailure {
                    if (it is CancellationException) throw it
                    Logger.w("Sync failed", it)
                    _syncState.value = SyncState.Error
                }
            }
            .flowOn(syncEngineDispatcher)
            .launchIn(syncEngineScope)
    }

    override suspend fun sync(forceRefresh: Boolean) {
        manualSyncTrigger.send(SyncRequest(forceRefresh))
    }

    private suspend fun performSync(syncDecision: SyncDecision) = coroutineScope {
        val (shouldSyncSources, shouldSyncItems) = syncDecision

        val feedSourcesDeferred = takeIf { shouldSyncSources }?.let {
            async { feedService.fetchFeedOrigins() }
        }

        val feedEntriesDeferred = takeIf { shouldSyncItems }?.let {
            async {
                feedService.fetchFeedEntries(
                    filters = db.feedOriginEntityQueries.allFeedOrigins().executeAsList().asNetworkModels()
                )
            }
        }

        val feedSources = feedSourcesDeferred?.await()
        val feedEntries = feedEntriesDeferred?.await()

        db.transaction {
            val currentFeedOrigins = db.feedOriginEntityQueries.allFeedOrigins().executeAsList()

            feedSources?.let { sources ->
                sources.forEach { source ->
                    val entity = source.toDbModel(currentFeedOrigins)
                    db.feedOriginEntityQueries.upsertFeedOrigin(
                        key = entity.key,
                        title = entity.title,
                        description = entity.description,
                        selected = entity.selected,
                    )
                }

                db.lastSyncMetadataQueries.updateLastSyncMetadata(
                    resource_type = SyncResourceType.FeedOrigins,
                    sync_params = "",
                    last_sync_time = clock.now(),
                )
            }

            feedEntries?.let { entries ->
                val currentFeedItems = db.feedItemEntityQueries.feedItemsForSelectedOrigins().executeAsList()
                entries.forEach { entry ->
                    val entity = entry.toDbModel(currentFeedItems)
                    db.feedItemEntityQueries.upsertFeedItem(
                        id = entity.id,
                        feed_origin_key = entity.feed_origin_key,
                        title = entity.title,
                        publish_time = entity.publish_time,
                        content_url = entity.content_url,
                        image_url = entity.image_url,
                        description = entity.description,
                        issue_number = entity.issue_number,
                        podcast_audio_url = entity.podcast_audio_url,
                        podcast_duration = entity.podcast_duration,
                        podcast_start_position = entity.podcast_start_position,
                        saved_for_later = entity.saved_for_later,
                    )
                }

                db.lastSyncMetadataQueries.updateLastSyncMetadata(
                    resource_type = SyncResourceType.FeedItems,
                    sync_params = currentFeedOrigins.ifEmpty {
                        // For the initial sync feed sources aren't available at the start of the transaction,
                        // but should be available at this point so we can load them again here.
                        // This makes sure we never write to the last_sync_metadata table with an empty sync_params.
                        db.feedOriginEntityQueries.allFeedOrigins().executeAsList()
                    }.toSyncParams(),
                    last_sync_time = clock.now(),
                )
            }
        }
    }
}
