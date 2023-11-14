package io.github.reactivecircus.kstreamlined.kmp.data.feed.model

sealed interface FeedItem {
    val id: String
    val title: String
    val publishTimestamp: String
    val contentUrl: String
    val savedForLater: Boolean

    data class KotlinBlog(
        override val id: String,
        override val title: String,
        override val publishTimestamp: String,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val featuredImageUrl: String,
        val description: String,
    ) : FeedItem

    data class KotlinYouTube(
        override val id: String,
        override val title: String,
        override val publishTimestamp: String,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val thumbnailUrl: String,
        val description: String,
    ) : FeedItem

    data class TalkingKotlin(
        override val id: String,
        override val title: String,
        override val publishTimestamp: String,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val podcastLogoUrl: String,
        val tags: List<String>,
    ) : FeedItem

    data class KotlinWeekly(
        override val id: String,
        override val title: String,
        override val publishTimestamp: String,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val newsletterLogoUrl: String,
    ) : FeedItem
}
