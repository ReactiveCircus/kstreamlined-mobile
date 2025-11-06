package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.prettytime.toFormattedTime
import kotlinx.datetime.TimeZone

internal fun FeedItem.TalkingKotlin.asPresentationModel(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): TalkingKotlinEpisode {
    return TalkingKotlinEpisode(
        id = id,
        title = title,
        displayablePublishTime = publishTime.toFormattedTime(timeZone),
        contentUrl = contentUrl,
        savedForLater = savedForLater,
        audioUrl = audioUrl,
        thumbnailUrl = thumbnailUrl,
        summary = summary,
        summaryIsHtml = summaryFormat == FeedItem.TalkingKotlin.ContentFormat.Html,
        duration = duration,
        startPositionMillis = startPositionMillis,
    )
}
