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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.datetime.Clock

public class FeedSyncEngineImpl(
    private val feedService: FeedService,
    private val db: KStreamlinedDatabase,
    syncEngineScope: CoroutineScope,
    dbDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val clock: Clock = Clock.System,
) : FeedSyncEngine {
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Syncing)
    override val syncState: StateFlow<SyncState> = _syncState

    private val manualSyncTrigger = Channel<SyncRequest>()

    private val automaticSyncTrigger = db.feedOriginEntityQueries
        .allFeedOrigins().asFlow().mapToList(dbDispatcher)
        // do not trigger until feed sources are loaded
        .takeWhile { it.isNotEmpty() }
        // skip initial emission to avoid performing both automatic sync and manual sync on app launch
        .drop(1)
        // map to sorted list of selected feed sources
        .map { origins ->
            origins.filter { it.selected }.sortedBy { it.key }
        }
        .distinctUntilChanged()
        .map { SyncRequest(forceRefresh = false, skipFeedSources = true) }

    private val syncDecisionMaker = SyncDecisionMaker(
        syncConfig = SyncConfig.Default,
        feedOriginEntityQueries = db.feedOriginEntityQueries,
        lastSyncMetadataQueries = db.lastSyncMetadataQueries,
        clock = clock,
    )

    init {
        merge(manualSyncTrigger.receiveAsFlow(), automaticSyncTrigger)
            .onEach { syncRequest ->
                _syncState.value = SyncState.Syncing
                performSync(syncRequest)
                _syncState.value = SyncState.Idle
            }
            .catch {
                Logger.w("Sync failed", it)
                _syncState.value = SyncState.Error
            }
            .launchIn(syncEngineScope)
    }

    override suspend fun sync(forceRefresh: Boolean) {
        manualSyncTrigger.send(SyncRequest(forceRefresh))
    }

    private suspend fun performSync(syncRequest: SyncRequest) = coroutineScope {
        val (shouldSyncSources, shouldSyncItems) = syncDecisionMaker.decide(syncRequest)

        val feedSourcesDeferred = if (shouldSyncSources) {
            async { feedService.fetchFeedOrigins() }
        } else { null }

        val feedEntriesDeferred = if (shouldSyncItems) {
            async {
                feedService.fetchFeedEntries(
                    filters = db.feedOriginEntityQueries.allFeedOrigins().executeAsList().asNetworkModels()
                )
            }
        } else { null }

        val feedSources = feedSourcesDeferred?.await()
        val feedEntries = feedEntriesDeferred?.await()

        if (feedSources == null && feedEntries == null) return@coroutineScope
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
                    sync_params = currentFeedOrigins.toSyncParams(),
                    last_sync_time = clock.now(),
                )
            }
        }
    }
}
