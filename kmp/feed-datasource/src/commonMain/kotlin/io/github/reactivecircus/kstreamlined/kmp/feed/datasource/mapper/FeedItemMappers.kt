package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin

internal fun FeedItemEntity.asExternalModel(): FeedItem {
    return when (feed_origin_key) {
        FeedOrigin.Key.KotlinBlog.name -> FeedItem.KotlinBlog(
            id = id,
            title = title,
            publishTime = publish_time,
            contentUrl = content_url,
            savedForLater = saved_for_later,
            featuredImageUrl = image_url!!,
        )

        FeedOrigin.Key.KotlinYouTubeChannel.name -> FeedItem.KotlinYouTube(
            id = id,
            title = title,
            publishTime = publish_time,
            contentUrl = content_url,
            savedForLater = saved_for_later,
            thumbnailUrl = image_url!!,
            description = description!!,
        )

        FeedOrigin.Key.TalkingKotlinPodcast.name -> FeedItem.TalkingKotlin(
            id = id,
            title = title,
            publishTime = publish_time,
            contentUrl = content_url,
            savedForLater = saved_for_later,
            audioUrl = podcast_audio_url!!,
            thumbnailUrl = image_url!!,
            summary = description!!,
            duration = podcast_duration!!,
            startPositionMillis = podcast_start_position ?: 0,
        )

        FeedOrigin.Key.KotlinWeekly.name -> FeedItem.KotlinWeekly(
            id = id,
            title = title,
            publishTime = publish_time,
            contentUrl = content_url,
            savedForLater = saved_for_later,
            issueNumber = issue_number!!.toInt(),
        )

        else -> error("Unknown feed origin key: $feed_origin_key")
    }
}
