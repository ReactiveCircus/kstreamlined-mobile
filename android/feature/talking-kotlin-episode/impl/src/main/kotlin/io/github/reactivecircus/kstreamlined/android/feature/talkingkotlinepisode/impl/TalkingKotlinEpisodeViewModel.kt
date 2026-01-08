package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl

import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodePresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodeUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodeUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@AssistedInject
public class TalkingKotlinEpisodeViewModel(
    @Assisted id: String,
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = TalkingKotlinEpisodePresenter(
        id = id,
        feedDataSource = feedDataSource,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
    )
    internal val uiState: StateFlow<TalkingKotlinEpisodeUiState> = presenter.states
    internal val eventSink: (TalkingKotlinEpisodeUiEvent) -> Unit = presenter.eventSink

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    public fun interface Factory : ManualViewModelAssistedFactory {
        public fun create(id: String): TalkingKotlinEpisodeViewModel
    }
}
