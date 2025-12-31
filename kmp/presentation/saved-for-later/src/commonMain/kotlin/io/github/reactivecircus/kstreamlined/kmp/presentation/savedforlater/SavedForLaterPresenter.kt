package io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.common.Presenter
import kotlinx.coroutines.CoroutineScope

public class SavedForLaterPresenter(
    private val feedDataSource: FeedDataSource,
    scope: CoroutineScope,
    recompositionMode: RecompositionMode = RecompositionMode.ContextClock,
) : Presenter<SavedForLaterUiEvent, SavedForLaterUiState>(scope, recompositionMode) {
    @Composable
    override fun present(): SavedForLaterUiState {
        var uiState by remember { mutableStateOf<SavedForLaterUiState>(SavedForLaterUiState.Loading) }
        LaunchedEffect(Unit) {
            feedDataSource.streamSavedFeedItems().collect { feedItems ->
                uiState = SavedForLaterUiState.Content(feedItems.toSavedForLaterFeedItems())
            }
        }
        CollectEvent { event ->
            when (event) {
                is SavedForLaterUiEvent.RemoveSavedItem -> {
                    feedDataSource.removeSavedFeedItem(event.id)
                }
            }
        }
        return uiState
    }
}
