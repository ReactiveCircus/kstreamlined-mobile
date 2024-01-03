package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

import io.github.reactivecircus.kstreamlined.kmp.data.feed.FeedRepository
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

public class TalkingKotlinEpisodePresenter(
    private val feedRepository: FeedRepository,
) {
    private val _uiState = MutableStateFlow<TalkingKotlinEpisodeUiState>(
        TalkingKotlinEpisodeUiState.Initializing
    )
    public val uiState: StateFlow<TalkingKotlinEpisodeUiState> = _uiState.asStateFlow()

    public suspend fun loadTalkingKotlinEpisode(id: String) {
        val talkingKotlinItem = feedRepository.loadFeedItemById(id) as? FeedItem.TalkingKotlin
            ?: error("Feed item not found")
        _uiState.value = TalkingKotlinEpisodeUiState.Content(
            episode = talkingKotlinItem.asPresentationModel(),
            isPlaying = false,
        )
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
}
