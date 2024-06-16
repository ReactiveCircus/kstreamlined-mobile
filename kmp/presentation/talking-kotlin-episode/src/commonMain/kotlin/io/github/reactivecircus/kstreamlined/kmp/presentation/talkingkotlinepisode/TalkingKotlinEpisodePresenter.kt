package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

public class TalkingKotlinEpisodePresenter(
    private val feedDataSource: FeedDataSource,
    scope: CoroutineScope,
) {
    public val uiState: StateFlow<TalkingKotlinEpisodeUiState>
        field = MutableStateFlow<TalkingKotlinEpisodeUiState>(TalkingKotlinEpisodeUiState.Initializing)

    public val eventSink: (TalkingKotlinEpisodeUiEvent) -> Unit = { scope.launch { processUiEvent(it) } }

    private suspend fun processUiEvent(event: TalkingKotlinEpisodeUiEvent) {
        when (event) {
            is TalkingKotlinEpisodeUiEvent.LoadEpisode -> {
                feedDataSource.streamFeedItemById(event.id)
                    .onStart { uiState.value = TalkingKotlinEpisodeUiState.Initializing }
                    .onEach { item ->
                        val talkingKotlinItem = item as? FeedItem.TalkingKotlin
                        if (talkingKotlinItem != null) {
                            uiState.value = TalkingKotlinEpisodeUiState.Content(
                                episode = talkingKotlinItem.asPresentationModel(),
                                isPlaying = false,
                            )
                        } else {
                            uiState.value = TalkingKotlinEpisodeUiState.NotFound
                        }
                    }
                    .collect()
            }

            is TalkingKotlinEpisodeUiEvent.ToggleSavedForLater -> {
                val episode = (uiState.value as? TalkingKotlinEpisodeUiState.Content)?.episode ?: return
                if (!episode.savedForLater) {
                    feedDataSource.addSavedFeedItem(episode.id)
                } else {
                    feedDataSource.removeSavedFeedItem(episode.id)
                }
            }

            is TalkingKotlinEpisodeUiEvent.SaveStartPosition -> {
                (uiState.value as? TalkingKotlinEpisodeUiState.Content)?.episode?.let { episode ->
                    feedDataSource.saveTalkingKotlinEpisodeStartPosition(episode.id, event.startPositionMillis)
                }
            }

            is TalkingKotlinEpisodeUiEvent.TogglePlayPause -> {
                uiState.update {
                    if (it is TalkingKotlinEpisodeUiState.Content) {
                        it.copy(isPlaying = !it.isPlaying)
                    } else {
                        it
                    }
                }
            }

            // TODO remove once ViewModel is scoped properly
            is TalkingKotlinEpisodeUiEvent.Reset -> {
                uiState.value = TalkingKotlinEpisodeUiState.Initializing
            }
        }
    }
}
