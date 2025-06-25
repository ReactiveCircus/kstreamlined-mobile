package io.github.reactivecircus.kstreamlined.kmp.model.feed

import kotlin.time.Instant

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
        val audioUrl: String,
        val thumbnailUrl: String,
        val summary: String,
        val summaryFormat: ContentFormat,
        val summaryPlainText: String?,
        val duration: String,
        val startPositionMillis: Long,
    ) : FeedItem {
        public enum class ContentFormat {
            Text,
            Html,
        }
    }

    public data class KotlinWeekly(
        override val id: String,
        override val title: String,
        override val publishTime: Instant,
        override val contentUrl: String,
        override val savedForLater: Boolean,
        val issueNumber: Int,
    ) : FeedItem
}
