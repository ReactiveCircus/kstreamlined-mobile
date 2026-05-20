package io.github.reactivecircus.kstreamlined.kmp.presentation.feedselection

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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@PresenterKey
@ContributesIntoMap(AppScope::class)
public class FeedSelectionPresenter(
    private val feedDataSource: FeedDataSource,
    moleculeContext: MoleculeContext,
) : Presenter<FeedSelectionUiEvent, FeedSelectionUiState>(moleculeContext) {
    @Composable
    override fun present(): FeedSelectionUiState {
        var uiState by remember { mutableStateOf<FeedSelectionUiState>(FeedSelectionUiState.Loading) }
        LaunchedEffect(Unit) {
            feedDataSource.streamFeedOrigins()
                .onEach { origins ->
                    uiState = FeedSelectionUiState.Content(feedOrigins = origins)
                }
                .collect()
        }
        CollectEvent { event ->
            when (event) {
                is FeedSelectionUiEvent.ToggleFeedOrigin -> {
                    val currentState = uiState
                    if (currentState is FeedSelectionUiState.Content) {
                        val origin = currentState.feedOrigins.first { it.key == event.key }
                        if (origin.selected) {
                            feedDataSource.unselectFeedSource(event.key)
                        } else {
                            feedDataSource.selectFeedSource(event.key)
                        }
                    }
                }
            }
        }
        return uiState
    }
}
