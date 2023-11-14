package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.graphql.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.graphql.type.FeedSourceKey
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedOrigin

internal fun FeedOrigin.Key.asApolloModel(): FeedSourceKey {
    return when (this) {
        FeedOrigin.Key.KotlinBlog -> FeedSourceKey.KOTLIN_BLOG
        FeedOrigin.Key.KotlinYouTubeChannel -> FeedSourceKey.KOTLIN_YOUTUBE_CHANNEL
        FeedOrigin.Key.TalkingKotlinPodcast -> FeedSourceKey.TALKING_KOTLIN_PODCAST
        FeedOrigin.Key.KotlinWeekly -> FeedSourceKey.KOTLIN_WEEKLY
    }
}

internal fun FeedSourcesQuery.FeedSource.asExternalModel(): FeedOrigin {
    return FeedOrigin(
        key = when (this.key) {
            FeedSourceKey.KOTLIN_BLOG -> FeedOrigin.Key.KotlinBlog
            FeedSourceKey.KOTLIN_YOUTUBE_CHANNEL -> FeedOrigin.Key.KotlinYouTubeChannel
            FeedSourceKey.TALKING_KOTLIN_PODCAST -> FeedOrigin.Key.TalkingKotlinPodcast
            FeedSourceKey.KOTLIN_WEEKLY -> FeedOrigin.Key.KotlinWeekly
            else -> error("Unknown FeedSourceKey subtype")
        },
        title = this.title,
        description = this.description,
    )
}
