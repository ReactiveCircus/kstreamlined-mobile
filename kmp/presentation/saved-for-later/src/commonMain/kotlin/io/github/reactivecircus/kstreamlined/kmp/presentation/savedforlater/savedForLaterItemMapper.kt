package io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater

import io.github.reactivecircus.kstreamlined.kmp.feed.model.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.toDisplayable
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
