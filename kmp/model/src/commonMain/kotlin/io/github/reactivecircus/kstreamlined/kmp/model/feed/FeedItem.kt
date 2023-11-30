package io.github.reactivecircus.kstreamlined.kmp.model.feed

import kotlinx.datetime.Instant

public sealed interface FeedItem {
    public val id: String
    public val title: String
    public val publishTime: Instant
    public val contentUrl: String
    public val savedForLater: Boolean

    public data class KotlinBlog(
        override val id: String,
        override val title: String,
        override val publishTime: Instant,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val featuredImageUrl: String,
    ) : FeedItem

    public data class KotlinYouTube(
        override val id: String,
        override val title: String,
        override val publishTime: Instant,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val thumbnailUrl: String,
        val description: String,
    ) : FeedItem

    public data class TalkingKotlin(
        override val id: String,
        override val title: String,
        override val publishTime: Instant,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val podcastLogoUrl: String,
    ) : FeedItem

    public data class KotlinWeekly(
        override val id: String,
        override val title: String,
        override val publishTime: Instant,
        override val contentUrl: String,
        override val savedForLater: Boolean,
    ) : FeedItem
}
