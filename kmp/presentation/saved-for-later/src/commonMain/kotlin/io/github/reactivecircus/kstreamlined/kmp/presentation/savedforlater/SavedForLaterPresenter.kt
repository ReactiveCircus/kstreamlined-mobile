package io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public class SavedForLaterPresenter(
    private val feedDataSource: FeedDataSource,
    scope: CoroutineScope,
) {
    private val _uiState = MutableStateFlow<SavedForLaterUiState>(SavedForLaterUiState.Loading)
    public val uiState: StateFlow<SavedForLaterUiState> = _uiState.asStateFlow()

    init {
        feedDataSource.streamSavedFeedItems()
            .onEach { feedItems ->
                _uiState.value = SavedForLaterUiState.Content(feedItems.toSavedForLaterFeedItems())
            }
            .launchIn(scope)
    }

    public suspend fun removeSavedItem(id: String) {
        feedDataSource.removeSavedFeedItem(id)
    }
}
