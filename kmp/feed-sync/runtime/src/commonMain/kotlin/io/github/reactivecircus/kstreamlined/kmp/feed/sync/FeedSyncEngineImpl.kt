package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import co.touchlab.kermit.Logger
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.SyncResourceType
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper.asNetworkModels
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper.toDbModel
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper.toSyncParams
import io.github.reactivecircus.kstreamlined.kmp.networkmonitor.NetworkMonitor
import io.github.reactivecircus.kstreamlined.kmp.networkmonitor.NetworkState
import io.github.reactivecircus.kstreamlined.kmp.remote.FeedService
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

public class FeedSyncEngineImpl(
    private val feedService: FeedService,
    private val db: KStreamlinedDatabase,
    networkMonitor: NetworkMonitor,
    syncEngineScope: CoroutineScope,
    syncEngineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val clock: Clock = Clock.System,
) : FeedSyncEngine {
    override val syncState: StateFlow<SyncState>
        field = MutableStateFlow<SyncState>(SyncState.Idle)

    private val manualSyncTrigger = Channel<SyncRequest>()

    private val dbChangeSyncTrigger = db.feedOriginEntityQueries
        .allFeedOrigins()
        .asFlow()
        .mapToList(syncEngineDispatcher)
        .map { SyncRequest(forceRefresh = false, skipFeedSources = it.isNotEmpty()) }

    private val networkStateChangeSyncTrigger = networkMonitor.networkState
        .filter {
            it == NetworkState.Connected && syncState.value is SyncState.OutOfSync
        }
        .map { SyncRequest(forceRefresh = false) }

    private val automaticSyncTrigger = merge(dbChangeSyncTrigger, networkStateChangeSyncTrigger)

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
                    val decision = syncRequestEvaluator.evaluate(
                        syncRequest = syncRequest,
                        lastSyncFailed = syncState.value is SyncState.OutOfSync,
                    )
                    if (decision.shouldSyncFeedSources || decision.shouldSyncFeedItems) {
                        syncState.value = SyncState.Syncing
                        performSync(decision)
                    }
                    syncState.value = SyncState.Idle
                }.onFailure {
                    if (it is CancellationException) currentCoroutineContext().ensureActive()
                    Logger.w("Sync failed", it)
                    syncState.value = SyncState.OutOfSync
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

        val feedSources: List<FeedSource>?
        val feedEntries: List<FeedEntry>?

        if (shouldSyncSources && shouldSyncItems) {
            // fetch both feed sources and feed items in a single request
            val (entries, sources) = feedService.fetchFeedEntriesAndOrigins()
            feedEntries = entries
            feedSources = sources
        } else if (shouldSyncSources) {
            feedSources = feedService.fetchFeedOrigins()
            feedEntries = null
        } else {
            feedSources = null
            feedEntries = feedService.fetchFeedEntries(
                filters = db.feedOriginEntityQueries.allFeedOrigins().executeAsList().asNetworkModels(),
            )
        }

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
                val currentFeedItems =
                    db.feedItemEntityQueries.feedItemsForSelectedOrigins().executeAsList()
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
                        podcast_description_format = entity.podcast_description_format,
                        podcast_description_plain_text = entity.podcast_description_plain_text,
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
