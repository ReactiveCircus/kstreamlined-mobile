package io.github.reactivecircus.kstreamlined.kmp.remote.mapper

import io.github.reactivecircus.kstreamlined.graphql.fragment.FeedSourceItem
import io.github.reactivecircus.kstreamlined.graphql.type.FeedSourceKey
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource

internal fun FeedSource.Key.asApolloModel(): FeedSourceKey {
    return when (this) {
        KotlinBlog -> KOTLIN_BLOG
        KotlinYouTubeChannel -> KOTLIN_YOUTUBE_CHANNEL
        TalkingKotlinPodcast -> TALKING_KOTLIN_PODCAST
        KotlinWeekly -> KOTLIN_WEEKLY
    }
}

internal fun FeedSourceItem.asExternalModel(): FeedSource? {
    return FeedSource(
        key = when (this.key) {
            KOTLIN_BLOG -> FeedSource.Key.KotlinBlog
            KOTLIN_YOUTUBE_CHANNEL -> FeedSource.Key.KotlinYouTubeChannel
            TALKING_KOTLIN_PODCAST -> FeedSource.Key.TalkingKotlinPodcast
            KOTLIN_WEEKLY -> FeedSource.Key.KotlinWeekly
            UNKNOWN__ -> return null
        },
        title = this.title,
        description = this.description,
    )
}
