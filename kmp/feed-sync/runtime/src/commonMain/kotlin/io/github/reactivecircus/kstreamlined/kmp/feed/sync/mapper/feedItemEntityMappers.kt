package io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper

import io.github.reactivecircus.kstreamlined.kmp.database.ContentFormat
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource

@Suppress("CyclomaticComplexMethod", "CognitiveComplexMethod")
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
            is KotlinBlog -> FeedSource.Key.KotlinBlog.name
            is KotlinYouTube -> FeedSource.Key.KotlinYouTubeChannel.name
            is TalkingKotlin -> FeedSource.Key.TalkingKotlinPodcast.name
            is KotlinWeekly -> FeedSource.Key.KotlinWeekly.name
        },
        title = title,
        publish_time = publishTime,
        content_url = contentUrl,
        image_url = when (this) {
            is KotlinBlog -> featuredImageUrl
            is KotlinYouTube -> thumbnailUrl
            is TalkingKotlin -> thumbnailUrl
            is KotlinWeekly -> null
        },
        description = when (this) {
            is KotlinBlog -> null
            is KotlinYouTube -> description
            is TalkingKotlin -> summary
            is KotlinWeekly -> null
        },
        issue_number = when (this) {
            is KotlinWeekly -> issueNumber.toLong()
            else -> null
        },
        podcast_audio_url = when (this) {
            is TalkingKotlin -> audioUrl
            else -> null
        },
        podcast_duration = when (this) {
            is TalkingKotlin -> duration
            else -> null
        },
        podcast_start_position = when (this) {
            is TalkingKotlin ->
                currentFeedItems.find { it.id == id }?.podcast_start_position
            else -> null
        },
        podcast_description_format = podcastDescriptionFormat,
        podcast_description_plain_text = podcastDescriptionPlainText,
        saved_for_later = currentFeedItems.any { it.saved_for_later && it.id == id },
    )
}
