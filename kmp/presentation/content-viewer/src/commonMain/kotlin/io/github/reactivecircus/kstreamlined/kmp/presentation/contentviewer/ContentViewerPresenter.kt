package io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

public class ContentViewerPresenter(
    private val feedDataSource: FeedDataSource,
    scope: CoroutineScope,
) {
    private val _uiState = MutableStateFlow<ContentViewerUiState>(
        ContentViewerUiState.Initializing
    )
    public val uiState: StateFlow<ContentViewerUiState> = _uiState.asStateFlow()

    public val eventSink: (ContentViewerUiEvent) -> Unit = { scope.launch { processUiEvent(it) } }

    private suspend fun processUiEvent(event: ContentViewerUiEvent) {
        when (event) {
            is ContentViewerUiEvent.LoadContent -> {
                feedDataSource.streamFeedItemById(event.id)
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

            is ContentViewerUiEvent.ToggleSavedForLater -> {
                val item = (uiState.value as? ContentViewerUiState.Content)?.item ?: return
                if (!item.savedForLater) {
                    feedDataSource.addSavedFeedItem(item.id)
                } else {
                    feedDataSource.removeSavedFeedItem(item.id)
                }
            }
        }
    }
}
