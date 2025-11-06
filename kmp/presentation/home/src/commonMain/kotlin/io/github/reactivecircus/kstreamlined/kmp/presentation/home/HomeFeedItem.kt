package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import androidx.compose.runtime.Immutable
import io.github.reactivecircus.kstreamlined.kmp.feed.model.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem

@Immutable
public sealed interface HomeFeedItem {
    public data class SectionHeader(val title: String) : HomeFeedItem

    public data class Item(val displayableFeedItem: DisplayableFeedItem<FeedItem>) : HomeFeedItem
}
