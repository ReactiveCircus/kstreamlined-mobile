package io.github.reactivecircus.kstreamlined.kmp.data.feed.mapper

import io.github.reactivecircus.kstreamlined.graphql.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.graphql.type.FeedSourceKey
import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedSource

internal fun FeedSource.Key.toApollo(): FeedSourceKey {
    return when (this) {
        FeedSource.Key.KotlinBlog -> FeedSourceKey.KOTLIN_BLOG
        FeedSource.Key.KotlinYouTubeChannel -> FeedSourceKey.KOTLIN_YOUTUBE_CHANNEL
        FeedSource.Key.TalkingKotlinPodcast -> FeedSourceKey.TALKING_KOTLIN_PODCAST
        FeedSource.Key.KotlinWeekly -> FeedSourceKey.KOTLIN_WEEKLY
    }
}

internal fun FeedSourcesQuery.FeedSource.toModel(): FeedSource {
    return FeedSource(
        key = when (this.key) {
            FeedSourceKey.KOTLIN_BLOG -> FeedSource.Key.KotlinBlog
            FeedSourceKey.KOTLIN_YOUTUBE_CHANNEL -> FeedSource.Key.KotlinYouTubeChannel
            FeedSourceKey.TALKING_KOTLIN_PODCAST -> FeedSource.Key.TalkingKotlinPodcast
            FeedSourceKey.KOTLIN_WEEKLY -> FeedSource.Key.KotlinWeekly
            else -> error("Unknown FeedSourceKey subtype")
        },
        title = this.title,
        description = this.description,
    )
}
