package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

public class TalkingKotlinEpisodePresenter(
    private val feedDataSource: FeedDataSource,
) {
    private val _uiState = MutableStateFlow<TalkingKotlinEpisodeUiState>(
        TalkingKotlinEpisodeUiState.Initializing
    )
    public val uiState: StateFlow<TalkingKotlinEpisodeUiState> = _uiState.asStateFlow()

    public suspend fun loadTalkingKotlinEpisode(id: String) {
        feedDataSource.streamFeedItemById(id)
            .onStart { _uiState.value = TalkingKotlinEpisodeUiState.Initializing }
            .onEach { item ->
                val talkingKotlinItem = item as? FeedItem.TalkingKotlin
                if (talkingKotlinItem != null) {
                    _uiState.value = TalkingKotlinEpisodeUiState.Content(
                        episode = talkingKotlinItem.asPresentationModel(),
                        isPlaying = false,
                    )
                } else {
                    _uiState.value = TalkingKotlinEpisodeUiState.NotFound
                }
            }
            .collect()
    }

    public suspend fun toggleSavedForLater() {
        val episode = (uiState.value as? TalkingKotlinEpisodeUiState.Content)?.episode ?: return
        if (!episode.savedForLater) {
            feedDataSource.addSavedFeedItem(episode.id)
        } else {
            feedDataSource.removeSavedFeedItem(episode.id)
        }
    }

    public suspend fun saveStartPosition(startPositionMillis: Long) {
        val id = (uiState.value as TalkingKotlinEpisodeUiState.Content).episode.id
        feedDataSource.saveTalkingKotlinEpisodeStartPosition(id, startPositionMillis)
    }

    public fun togglePlayPause() {
        _uiState.update {
            if (it is TalkingKotlinEpisodeUiState.Content) {
                it.copy(isPlaying = !it.isPlaying)
            } else {
                it
            }
        }
    }

    // TODO remove once ViewModel is scoped properly
    public fun reset() {
        _uiState.value = TalkingKotlinEpisodeUiState.Initializing
    }
}
