package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.presentation.common.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

public class TalkingKotlinEpisodePresenter(
    private val feedDataSource: FeedDataSource,
    scope: CoroutineScope,
    recompositionMode: RecompositionMode,
) : Presenter<TalkingKotlinEpisodeUiEvent, TalkingKotlinEpisodeUiState>(scope, recompositionMode) {
    @Composable
    override fun present(): TalkingKotlinEpisodeUiState {
        var uiState by remember {
            mutableStateOf<TalkingKotlinEpisodeUiState>(TalkingKotlinEpisodeUiState.Initializing)
        }
        var itemId by remember { mutableStateOf<String?>(null) }
        LaunchedEffect(itemId) {
            itemId?.let { id ->
                feedDataSource.streamFeedItemById(id)
                    .onStart { uiState = TalkingKotlinEpisodeUiState.Initializing }
                    .onEach { item ->
                        val talkingKotlinItem = item as? FeedItem.TalkingKotlin
                        uiState = if (talkingKotlinItem != null) {
                            TalkingKotlinEpisodeUiState.Content(
                                episode = talkingKotlinItem.asPresentationModel(),
                                isPlaying = false,
                            )
                        } else {
                            TalkingKotlinEpisodeUiState.NotFound
                        }
                    }
                    .collect()
            }
        }
        CollectEvent { event ->
            when (event) {
                is TalkingKotlinEpisodeUiEvent.LoadEpisode -> {
                    itemId = event.id
                }

                is TalkingKotlinEpisodeUiEvent.ToggleSavedForLater -> {
                    val episode = (uiState as? TalkingKotlinEpisodeUiState.Content)?.episode
                        ?: return@CollectEvent
                    if (!episode.savedForLater) {
                        feedDataSource.addSavedFeedItem(episode.id)
                    } else {
                        feedDataSource.removeSavedFeedItem(episode.id)
                    }
                }

                is TalkingKotlinEpisodeUiEvent.SaveStartPosition -> {
                    (uiState as? TalkingKotlinEpisodeUiState.Content)?.episode?.let { episode ->
                        feedDataSource.saveTalkingKotlinEpisodeStartPosition(
                            id = episode.id,
                            positionMillis = event.startPositionMillis,
                        )
                    }
                }

                is TalkingKotlinEpisodeUiEvent.TogglePlayPause -> {
                    val currentUiState = uiState
                    if (currentUiState is TalkingKotlinEpisodeUiState.Content) {
                        uiState = currentUiState.copy(isPlaying = !currentUiState.isPlaying)
                    }
                }

                // TODO remove once ViewModel is scoped properly
                is TalkingKotlinEpisodeUiEvent.Reset -> {
                    itemId = null
                }
            }
        }
        return uiState
    }
}
