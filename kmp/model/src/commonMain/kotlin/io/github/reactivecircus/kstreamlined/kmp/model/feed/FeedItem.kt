package io.github.reactivecircus.kstreamlined.kmp.model.feed

public sealed interface FeedItem {
    public val id: String
    public val title: String
    public val publishTime: String
    public val contentUrl: String
    public val savedForLater: Boolean

    public data class KotlinBlog(
        override val id: String,
        override val title: String,
        override val publishTime: String,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val featuredImageUrl: String?,
        val description: String,
    ) : FeedItem

    public data class KotlinYouTube(
        override val id: String,
        override val title: String,
        override val publishTime: String,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val thumbnailUrl: String,
        val description: String,
    ) : FeedItem

    public data class TalkingKotlin(
        override val id: String,
        override val title: String,
        override val publishTime: String,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val podcastLogoUrl: String,
        val tags: List<String>,
    ) : FeedItem

    public data class KotlinWeekly(
        override val id: String,
        override val title: String,
        override val publishTime: String,
        override val contentUrl: String,
        override val savedForLater: Boolean,
    ) : FeedItem
}
