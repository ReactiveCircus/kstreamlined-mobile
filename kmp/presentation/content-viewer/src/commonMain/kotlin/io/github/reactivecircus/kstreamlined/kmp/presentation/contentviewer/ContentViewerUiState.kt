package io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer

import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem

public sealed interface ContentViewerUiState {
    public data object Initializing : ContentViewerUiState
    public data object NotFound : ContentViewerUiState
    public data class Content(val item: FeedItem) : ContentViewerUiState
}
