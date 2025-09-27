package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import androidx.compose.runtime.Immutable
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem

@Immutable
public sealed interface HomeUiState {
    public data object Loading : HomeUiState

    public data class Content(
        val selectedFeedCount: Int,
        val feedItems: List<HomeFeedItem>,
        val refreshing: Boolean,
        val hasTransientError: Boolean,
    ) : HomeUiState

    public data object Error : HomeUiState
}

public sealed interface HomeUiEvent {
    public data class ToggleSavedForLater(val item: FeedItem) : HomeUiEvent

    public data object Refresh : HomeUiEvent

    public data object DismissTransientError : HomeUiEvent
}
