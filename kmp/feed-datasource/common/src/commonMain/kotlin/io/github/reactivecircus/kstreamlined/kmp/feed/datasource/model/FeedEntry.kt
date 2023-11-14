package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model

sealed interface FeedEntry {
    val id: String
    val title: String
    val publishTimestamp: String
    val contentUrl: String

    data class KotlinBlog(
        override val id: String,
        override val title: String,
        override val publishTimestamp: String,
        override val contentUrl: String,
        val featuredImageUrl: String,
        val description: String,
    ) : FeedEntry

    data class KotlinYouTube(
        override val id: String,
        override val title: String,
        override val publishTimestamp: String,
        override val contentUrl: String,
        val thumbnailUrl: String,
        val description: String,
    ) : FeedEntry

    data class TalkingKotlin(
        override val id: String,
        override val title: String,
        override val publishTimestamp: String,
        override val contentUrl: String,
        val podcastLogoUrl: String,
        val tags: List<String>,
    ) : FeedEntry

    data class KotlinWeekly(
        override val id: String,
        override val title: String,
        override val publishTimestamp: String,
        override val contentUrl: String,
        val newsletterLogoUrl: String,
    ) : FeedEntry
}
