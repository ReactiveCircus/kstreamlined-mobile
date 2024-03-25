package io.github.reactivecircus.kstreamlined.kmp.database.testing

import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntity
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.SyncResourceType
import kotlinx.datetime.Instant

public fun KStreamlinedDatabase.insertFeedOrigins(feedOrigins: List<FeedOriginEntity>) {
    transaction {
        feedOrigins.forEach {
            feedOriginEntityQueries.upsertFeedOrigin(
                key = it.key,
                title = it.title,
                description = it.description,
                selected = it.selected,
            )
        }
    }
}

public fun KStreamlinedDatabase.insertFeedItems(feedItems: List<FeedItemEntity>) {
    transaction {
        feedItems.forEach {
            feedItemEntityQueries.upsertFeedItem(
                id = it.id,
                feed_origin_key = it.feed_origin_key,
                title = it.title,
                publish_time = it.publish_time,
                content_url = it.content_url,
                image_url = it.image_url,
                description = it.description,
                issue_number = it.issue_number,
                podcast_audio_url = it.podcast_audio_url,
                podcast_duration = it.podcast_duration,
                podcast_start_position = it.podcast_start_position,
                saved_for_later = it.saved_for_later,
            )
        }
    }
}

public fun KStreamlinedDatabase.insertFeedOriginsLastSyncMetadata(lastSyncTime: Instant) {
    lastSyncMetadataQueries.updateLastSyncMetadata(
        resource_type = SyncResourceType.FeedOrigins,
        sync_params = "",
        last_sync_time = lastSyncTime,
    )
}

public fun KStreamlinedDatabase.insertFeedItemsLastSyncMetadata(
    syncParams: String,
    lastSyncTime: Instant,
) {
    lastSyncMetadataQueries.updateLastSyncMetadata(
        resource_type = SyncResourceType.FeedItems,
        sync_params = syncParams,
        last_sync_time = lastSyncTime,
    )
}
