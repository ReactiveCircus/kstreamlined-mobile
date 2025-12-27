package io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer

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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

public class ContentViewerPresenter(
    private val id: String,
    private val feedDataSource: FeedDataSource,
    scope: CoroutineScope,
    recompositionMode: RecompositionMode,
) : Presenter<ContentViewerUiEvent, ContentViewerUiState>(scope, recompositionMode) {
    @Composable
    override fun present(): ContentViewerUiState {
        var uiState by remember { mutableStateOf<ContentViewerUiState>(ContentViewerUiState.Initializing) }
        LaunchedEffect(Unit) {
            feedDataSource.streamFeedItemById(id)
                .onStart { uiState = ContentViewerUiState.Initializing }
                .onEach { item ->
                    uiState = if (item != null) {
                        ContentViewerUiState.Content(item)
                    } else {
                        ContentViewerUiState.NotFound
                    }
                }
                .collect()
        }
        CollectEvent { event ->
            when (event) {
                is ContentViewerUiEvent.ToggleSavedForLater -> {
                    val item = (uiState as? ContentViewerUiState.Content)?.item
                        ?: return@CollectEvent
                    if (!item.savedForLater) {
                        feedDataSource.addSavedFeedItem(item.id)
                    } else {
                        feedDataSource.removeSavedFeedItem(item.id)
                    }
                }
            }
        }
        return uiState
    }
}
