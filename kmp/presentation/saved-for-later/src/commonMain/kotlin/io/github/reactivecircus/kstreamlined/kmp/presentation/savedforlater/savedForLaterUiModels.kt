package io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater

import androidx.compose.runtime.Immutable
import io.github.reactivecircus.kstreamlined.kmp.feed.model.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem

@Immutable
public sealed interface SavedForLaterUiState {
    public data object Loading : SavedForLaterUiState

    public data class Content(
        val feedItems: List<DisplayableFeedItem<FeedItem>>,
    ) : SavedForLaterUiState
}

public sealed interface SavedForLaterUiEvent {
    public data class RemoveSavedItem(val id: String) : SavedForLaterUiEvent
}
