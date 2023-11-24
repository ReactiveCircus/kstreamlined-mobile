package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model

import kotlinx.datetime.Instant

public sealed interface FeedEntry {
    public val id: String
    public val title: String
    public val publishTime: Instant
    public val contentUrl: String

    public data class KotlinBlog(
        override val id: String,
        override val title: String,
        override val publishTime: Instant,
        override val contentUrl: String,
        val featuredImageUrl: String?,
        val description: String,
    ) : FeedEntry

    public data class KotlinYouTube(
        override val id: String,
        override val title: String,
        override val publishTime: Instant,
        override val contentUrl: String,
        val thumbnailUrl: String,
        val description: String,
    ) : FeedEntry

    public data class TalkingKotlin(
        override val id: String,
        override val title: String,
        override val publishTime: Instant,
        override val contentUrl: String,
        val podcastLogoUrl: String,
        val tags: List<String>,
    ) : FeedEntry

    public data class KotlinWeekly(
        override val id: String,
        override val title: String,
        override val publishTime: Instant,
        override val contentUrl: String,
    ) : FeedEntry
}
