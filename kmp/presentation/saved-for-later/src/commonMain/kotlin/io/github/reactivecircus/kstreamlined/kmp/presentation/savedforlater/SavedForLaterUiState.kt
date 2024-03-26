package io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater

import io.github.reactivecircus.kstreamlined.kmp.model.feed.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem

public sealed interface SavedForLaterUiState {
    public data object Loading : SavedForLaterUiState
    public data class Content(
        val feedItems: List<DisplayableFeedItem<FeedItem>>,
    ) : SavedForLaterUiState
}
