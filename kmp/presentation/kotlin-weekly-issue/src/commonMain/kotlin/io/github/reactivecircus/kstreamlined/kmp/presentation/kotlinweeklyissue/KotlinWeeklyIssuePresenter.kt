package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import io.github.reactivecircus.kstreamlined.kmp.data.feed.FeedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

public class KotlinWeeklyIssuePresenter(
    private val feedRepository: FeedRepository,
) {
    private val _uiState = MutableStateFlow<KotlinWeeklyIssueUiState>(
        KotlinWeeklyIssueUiState.InFlight
    )
    public val uiState: StateFlow<KotlinWeeklyIssueUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    public suspend fun loadKotlinWeeklyIssue(id: String) {
        feedRepository.streamFeedItemById(id)
            .onStart { _uiState.value = KotlinWeeklyIssueUiState.InFlight }
            .mapLatest { item ->
                item ?: error("Feed item not found")
                item to feedRepository.loadKotlinWeeklyIssue(item.contentUrl)
            }
            .onEach { (item, issue) ->
                _uiState.value = KotlinWeeklyIssueUiState.Content(
                    id = item.id,
                    contentUrl = item.contentUrl,
                    issueItems = issue.groupBy { it.group },
                    savedForLater = item.savedForLater
                )
            }
            .catch {
                _uiState.value = KotlinWeeklyIssueUiState.Error
            }.collect()
    }

    public suspend fun toggleSavedForLater() {
        val state = (uiState.value as? KotlinWeeklyIssueUiState.Content) ?: return
        if (!state.savedForLater) {
            feedRepository.addSavedFeedItem(state.id)
        } else {
            feedRepository.removeSavedFeedItem(state.id)
        }
    }
}
