package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntityQueries
import io.github.reactivecircus.kstreamlined.kmp.database.LastSyncMetadataQueries
import io.github.reactivecircus.kstreamlined.kmp.database.SyncResourceType
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper.toSyncParams
import kotlin.time.Clock

internal class SyncRequestEvaluator(
    private val syncConfig: SyncConfig,
    private val feedOriginEntityQueries: FeedOriginEntityQueries,
    private val lastSyncMetadataQueries: LastSyncMetadataQueries,
    private val clock: Clock,
) {
    fun evaluate(syncRequest: SyncRequest, lastSyncFailed: Boolean): SyncDecision {
        return SyncDecision(
            shouldSyncFeedSources = lastSyncFailed || !syncRequest.skipFeedSources &&
                (syncRequest.forceRefresh || shouldSyncFeedSources()),
            shouldSyncFeedItems = lastSyncFailed || syncRequest.forceRefresh || shouldSyncFeedItems(),
        )
    }

    private fun shouldSyncFeedSources(): Boolean {
        val lastSyncTime = lastSyncMetadataQueries
            .lastSyncMetadata(SyncResourceType.FeedOrigins, "")
            .executeAsOneOrNull()?.last_sync_time
        return lastSyncTime == null ||
            (clock.now() - lastSyncTime) > syncConfig.feedSourcesCacheMaxAge
    }

    private fun shouldSyncFeedItems(): Boolean {
        val currentSyncParams = feedOriginEntityQueries.allFeedOrigins().executeAsList().toSyncParams()
        val itemsSyncMetadata = lastSyncMetadataQueries
            .lastSyncMetadata(SyncResourceType.FeedItems, currentSyncParams)
            .executeAsOneOrNull()
        return itemsSyncMetadata == null ||
            (clock.now() - itemsSyncMetadata.last_sync_time) > syncConfig.feedItemsCacheMaxAge
    }
}

internal data class SyncDecision(
    val shouldSyncFeedSources: Boolean,
    val shouldSyncFeedItems: Boolean,
)
