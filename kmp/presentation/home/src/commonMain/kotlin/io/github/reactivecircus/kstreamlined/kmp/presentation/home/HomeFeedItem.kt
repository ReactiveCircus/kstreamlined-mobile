package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import io.github.reactivecircus.kstreamlined.kmp.model.feed.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem

internal sealed interface HomeFeedItem {
    data class SectionHeader(val title: String) : HomeFeedItem
    data class Item(val displayableFeedItem: DisplayableFeedItem<FeedItem>) : HomeFeedItem
}
