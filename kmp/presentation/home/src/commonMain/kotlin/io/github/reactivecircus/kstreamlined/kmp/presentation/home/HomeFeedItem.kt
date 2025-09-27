package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import androidx.compose.runtime.Immutable
import io.github.reactivecircus.kstreamlined.kmp.model.feed.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem

@Immutable
public sealed interface HomeFeedItem {
    public data class SectionHeader(val title: String) : HomeFeedItem

    public data class Item(val displayableFeedItem: DisplayableFeedItem<FeedItem>) : HomeFeedItem
}
