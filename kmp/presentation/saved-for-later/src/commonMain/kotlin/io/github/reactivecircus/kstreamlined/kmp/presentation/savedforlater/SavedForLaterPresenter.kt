package io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

public class SavedForLaterPresenter(
    private val feedDataSource: FeedDataSource,
    scope: CoroutineScope,
) {
    public val uiState: StateFlow<SavedForLaterUiState>
        field = MutableStateFlow<SavedForLaterUiState>(SavedForLaterUiState.Loading)

    public val eventSink: (SavedForLaterUiEvent) -> Unit = { scope.launch { processUiEvent(it) } }

    init {
        feedDataSource.streamSavedFeedItems()
            .onEach { feedItems ->
                uiState.value = SavedForLaterUiState.Content(feedItems.toSavedForLaterFeedItems())
            }
            .launchIn(scope)
    }

    private suspend fun processUiEvent(event: SavedForLaterUiEvent) {
        when (event) {
            is SavedForLaterUiEvent.RemoveSavedItem -> {
                feedDataSource.removeSavedFeedItem(event.id)
            }
        }
    }
}
