package io.github.reactivecircus.kstreamlined.kmp.remote.model

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
        val featuredImageUrl: String,
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
        val audioUrl: String,
        val thumbnailUrl: String,
        val summary: String,
        val duration: String,
    ) : FeedEntry

    public data class KotlinWeekly(
        override val id: String,
        override val title: String,
        override val publishTime: Instant,
        override val contentUrl: String,
        val issueNumber: Int,
    ) : FeedEntry
}
