package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.data.feed.FeedRepository
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssuePresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class KotlinWeeklyIssueViewModel @Inject constructor(
    feedRepository: FeedRepository,
) : ViewModel() {
    private val presenter = KotlinWeeklyIssuePresenter(feedRepository)
    val uiState: StateFlow<KotlinWeeklyIssueUiState> = presenter.uiState

    fun loadKotlinWeeklyIssue(id: String) = viewModelScope.launch {
        presenter.loadKotlinWeeklyIssue(id)
    }

    fun toggleSavedForLater() = viewModelScope.launch {
        presenter.toggleSavedForLater()
    }
}
