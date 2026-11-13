package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo

import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.prettytime.toFormattedTime
import kotlinx.datetime.TimeZone

context(timeZone: TimeZone)
internal fun FeedItem.KotlinYouTube.asPresentationModel(): KotlinYouTubeVideo {
    return KotlinYouTubeVideo(
        id = id,
        title = title,
        displayablePublishTime = publishTime.toFormattedTime(),
        contentUrl = contentUrl,
        savedForLater = savedForLater,
        thumbnailUrl = thumbnailUrl,
        description = description,
        videoId = videoId,
    )
}
