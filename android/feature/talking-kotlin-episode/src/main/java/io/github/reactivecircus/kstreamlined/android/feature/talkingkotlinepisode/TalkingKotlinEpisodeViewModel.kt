package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.data.feed.FeedRepository
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodePresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodeUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TalkingKotlinEpisodeViewModel @Inject constructor(
    feedRepository: FeedRepository,
) : ViewModel() {
    private val presenter = TalkingKotlinEpisodePresenter(feedRepository)
    val uiState: StateFlow<TalkingKotlinEpisodeUiState> = presenter.uiState

    fun loadTalkingKotlinEpisode(id: String) = viewModelScope.launch {
        presenter.loadTalkingKotlinEpisode(id)
    }

    fun saveStartPosition(startPositionMillis: Long) = viewModelScope.launch {
        presenter.saveStartPosition(startPositionMillis)
    }

    fun togglePlayPause() = presenter.togglePlayPause()

    fun reset() = presenter.reset()
}
