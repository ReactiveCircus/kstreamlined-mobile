package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntity
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin

internal fun FeedOriginEntity.asExternalModel(): FeedOrigin {
    return FeedOrigin(
        key = when (key) {
            FeedOrigin.Key.KotlinBlog.name -> FeedOrigin.Key.KotlinBlog
            FeedOrigin.Key.KotlinYouTubeChannel.name -> FeedOrigin.Key.KotlinYouTubeChannel
            FeedOrigin.Key.TalkingKotlinPodcast.name -> FeedOrigin.Key.TalkingKotlinPodcast
            FeedOrigin.Key.KotlinWeekly.name -> FeedOrigin.Key.KotlinWeekly
            else -> error("Unknown feed origin key: $key")
        },
        title = title,
        description = description,
        selected = selected,
    )
}
