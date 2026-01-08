package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.impl

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
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssuePresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@AssistedInject
public class KotlinWeeklyIssueViewModel(
    @Assisted id: String,
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = KotlinWeeklyIssuePresenter(
        id = id,
        feedDataSource = feedDataSource,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
    )
    internal val uiState: StateFlow<KotlinWeeklyIssueUiState> = presenter.states
    internal val eventSink: (KotlinWeeklyIssueUiEvent) -> Unit = presenter.eventSink

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    public fun interface Factory : ManualViewModelAssistedFactory {
        public fun create(id: String): KotlinWeeklyIssueViewModel
    }
}
