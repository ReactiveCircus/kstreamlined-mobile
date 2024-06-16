package io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

public class ContentViewerPresenter(
    private val feedDataSource: FeedDataSource,
    scope: CoroutineScope,
) {
    public val uiState: StateFlow<ContentViewerUiState>
        field = MutableStateFlow<ContentViewerUiState>(ContentViewerUiState.Initializing)

    public val eventSink: (ContentViewerUiEvent) -> Unit = { scope.launch { processUiEvent(it) } }

    private suspend fun processUiEvent(event: ContentViewerUiEvent) {
        when (event) {
            is ContentViewerUiEvent.LoadContent -> {
                feedDataSource.streamFeedItemById(event.id)
                    .onStart { uiState.value = ContentViewerUiState.Initializing }
                    .onEach { item ->
                        if (item != null) {
                            uiState.value = ContentViewerUiState.Content(item)
                        } else {
                            uiState.value = ContentViewerUiState.NotFound
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
