package io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater

import io.github.reactivecircus.kstreamlined.kmp.model.feed.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import io.github.reactivecircus.kstreamlined.kmp.prettytime.toFormattedTime
import kotlinx.datetime.TimeZone

internal fun List<FeedItem>.toSavedForLaterFeedItems(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): List<DisplayableFeedItem<FeedItem>> {
    return map {
        it.toDisplayable(
            it.publishTime.toFormattedTime(timeZone),
        )
    }
}
