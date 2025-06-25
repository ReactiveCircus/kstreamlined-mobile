package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import io.github.reactivecircus.kstreamlined.kmp.prettytime.timeAgo
import io.github.reactivecircus.kstreamlined.kmp.prettytime.weeksAgo
import kotlinx.datetime.TimeZone
import kotlin.time.Clock

internal fun List<FeedItem>.toHomeFeedItems(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): List<HomeFeedItem> {
    val homeFeedItems = mutableListOf<HomeFeedItem>()
    var currentSectionHeader: String? = null

    // assume items are already sorted by publish time in descending order
    forEach { feedItem ->
        val sectionHeader = feedItem.publishTime.weeksAgo(clock)
        if (sectionHeader != currentSectionHeader) {
            homeFeedItems.add(HomeFeedItem.SectionHeader(sectionHeader))
            currentSectionHeader = sectionHeader
        }
        val displayableFeedItem = feedItem.toDisplayable(
            feedItem.publishTime.timeAgo(clock, timeZone)
        )
        homeFeedItems.add(HomeFeedItem.Item(displayableFeedItem))
    }

    return homeFeedItems
}
