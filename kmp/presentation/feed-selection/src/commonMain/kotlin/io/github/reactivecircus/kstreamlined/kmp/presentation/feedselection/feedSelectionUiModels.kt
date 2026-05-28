package io.github.reactivecircus.kstreamlined.kmp.presentation.feedselection

import androidx.compose.runtime.Immutable
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin

@Immutable
public sealed interface FeedSelectionUiState {
    public data object Loading : FeedSelectionUiState

    public data class Content(
        val feedOrigins: List<FeedOrigin>,
    ) : FeedSelectionUiState
}

public sealed interface FeedSelectionUiEvent {
    public data class ToggleFeedOrigin(val key: FeedOrigin.Key) : FeedSelectionUiEvent
}
