package io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.MoleculeContext
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.Presenter
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterKey
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource

@PresenterKey
@ContributesIntoMap(AppScope::class)
public class SavedForLaterPresenter(
    private val feedDataSource: FeedDataSource,
    moleculeContext: MoleculeContext,
) : Presenter<SavedForLaterUiEvent, SavedForLaterUiState>(moleculeContext) {
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
