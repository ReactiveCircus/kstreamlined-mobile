package io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper

import io.github.reactivecircus.kstreamlined.kmp.database.ContentFormat
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource

@Suppress("CyclomaticComplexMethod")
internal fun FeedEntry.toDbModel(
    currentFeedItems: List<FeedItemEntity>,
): FeedItemEntity {
    val (podcastDescriptionFormat, podcastDescriptionPlainText) = if (this is FeedEntry.TalkingKotlin) {
        val parsed = tryParseHtml(summary)
        val summaryFormat = if (parsed != null) ContentFormat.Html else ContentFormat.Text
        val summaryPlainText = parsed?.text()
        summaryFormat to summaryPlainText
    } else {
        null to null
    }
    return FeedItemEntity(
        id = id,
        feed_origin_key = when (this) {
            is FeedEntry.KotlinBlog -> FeedSource.Key.KotlinBlog.name
            is FeedEntry.KotlinYouTube -> FeedSource.Key.KotlinYouTubeChannel.name
            is FeedEntry.TalkingKotlin -> FeedSource.Key.TalkingKotlinPodcast.name
            is FeedEntry.KotlinWeekly -> FeedSource.Key.KotlinWeekly.name
        },
        title = title,
        publish_time = publishTime,
        content_url = contentUrl,
        image_url = when (this) {
            is FeedEntry.KotlinBlog -> featuredImageUrl
            is FeedEntry.KotlinYouTube -> thumbnailUrl
            is FeedEntry.TalkingKotlin -> thumbnailUrl
            is FeedEntry.KotlinWeekly -> null
        },
        description = when (this) {
            is FeedEntry.KotlinBlog -> null
            is FeedEntry.KotlinYouTube -> description
            is FeedEntry.TalkingKotlin -> summary
            is FeedEntry.KotlinWeekly -> null
        },
        issue_number = when (this) {
            is FeedEntry.KotlinWeekly -> issueNumber.toLong()
            else -> null
        },
        podcast_audio_url = when (this) {
            is FeedEntry.TalkingKotlin -> audioUrl
            else -> null
        },
        podcast_duration = when (this) {
            is FeedEntry.TalkingKotlin -> duration
            else -> null
        },
        podcast_start_position = when (this) {
            is FeedEntry.TalkingKotlin ->
                currentFeedItems.find { it.id == id }?.podcast_start_position
            else -> null
        },
        podcast_description_format = podcastDescriptionFormat,
        podcast_description_plain_text = podcastDescriptionPlainText,
        saved_for_later = currentFeedItems.any { it.saved_for_later && it.id == id },
    )
}
