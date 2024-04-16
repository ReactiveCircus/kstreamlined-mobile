package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodePresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodeUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodeUiState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class TalkingKotlinEpisodeViewModel @Inject constructor(
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = TalkingKotlinEpisodePresenter(feedDataSource, viewModelScope)
    val uiState: StateFlow<TalkingKotlinEpisodeUiState> = presenter.uiState
    val eventSink: (TalkingKotlinEpisodeUiEvent) -> Unit = presenter.eventSink
}
