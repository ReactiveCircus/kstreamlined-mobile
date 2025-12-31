package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.impl

import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssuePresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel(assistedFactory = KotlinWeeklyIssueViewModel.Factory::class)
internal class KotlinWeeklyIssueViewModel @AssistedInject constructor(
    @Assisted id: String,
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = KotlinWeeklyIssuePresenter(
        id = id,
        feedDataSource = feedDataSource,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
    )
    val uiState: StateFlow<KotlinWeeklyIssueUiState> = presenter.states
    val eventSink: (KotlinWeeklyIssueUiEvent) -> Unit = presenter.eventSink

    @AssistedFactory
    interface Factory {
        fun create(id: String): KotlinWeeklyIssueViewModel
    }
}
