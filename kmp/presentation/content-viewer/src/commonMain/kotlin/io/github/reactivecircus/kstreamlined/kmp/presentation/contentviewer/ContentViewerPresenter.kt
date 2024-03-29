package io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

public class ContentViewerPresenter(
    private val feedDataSource: FeedDataSource,
) {
    private val _uiState = MutableStateFlow<ContentViewerUiState>(
        ContentViewerUiState.Initializing
    )
    public val uiState: StateFlow<ContentViewerUiState> = _uiState.asStateFlow()

    public suspend fun loadContent(id: String) {
        feedDataSource.streamFeedItemById(id)
            .onStart { _uiState.value = ContentViewerUiState.Initializing }
            .onEach { item ->
                if (item != null) {
                    _uiState.value = ContentViewerUiState.Content(item)
                } else {
                    _uiState.value = ContentViewerUiState.NotFound
                }
            }
            .collect()
    }

    public suspend fun toggleSavedForLater() {
        val item = (uiState.value as? ContentViewerUiState.Content)?.item ?: return
        if (!item.savedForLater) {
            feedDataSource.addSavedFeedItem(item.id)
        } else {
            feedDataSource.removeSavedFeedItem(item.id)
        }
    }
}
