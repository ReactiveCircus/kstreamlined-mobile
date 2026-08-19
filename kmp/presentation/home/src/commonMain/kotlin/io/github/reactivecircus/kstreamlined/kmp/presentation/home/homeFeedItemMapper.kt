package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.toDisplayable
import io.github.reactivecircus.kstreamlined.kmp.prettytime.timeAgo
import io.github.reactivecircus.kstreamlined.kmp.prettytime.weeksAgo
import kotlinx.datetime.TimeZone
import kotlin.time.Clock

context(clock: Clock, timeZone: TimeZone)
internal fun List<FeedItem>.toHomeFeedItems(): List<HomeFeedItem> {
    val homeFeedItems = mutableListOf<HomeFeedItem>()
    var currentSectionHeader: String? = null

    // assume items are already sorted by publish time in descending order
    forEach { feedItem ->
        val sectionHeader = feedItem.publishTime.weeksAgo()
        if (sectionHeader != currentSectionHeader) {
            homeFeedItems.add(HomeFeedItem.SectionHeader(sectionHeader))
            currentSectionHeader = sectionHeader
        }
        val displayableFeedItem = feedItem.toDisplayable(
            feedItem.publishTime.timeAgo(),
        )
        homeFeedItems.add(HomeFeedItem.Item(displayableFeedItem))
    }

    return homeFeedItems
}
