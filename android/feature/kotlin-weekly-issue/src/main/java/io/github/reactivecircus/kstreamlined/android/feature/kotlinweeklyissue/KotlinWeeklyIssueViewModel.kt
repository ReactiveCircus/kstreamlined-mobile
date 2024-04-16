package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssuePresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class KotlinWeeklyIssueViewModel @Inject constructor(
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = KotlinWeeklyIssuePresenter(feedDataSource, viewModelScope)
    val uiState: StateFlow<KotlinWeeklyIssueUiState> = presenter.uiState
    val eventSink: (KotlinWeeklyIssueUiEvent) -> Unit = presenter.eventSink
}
