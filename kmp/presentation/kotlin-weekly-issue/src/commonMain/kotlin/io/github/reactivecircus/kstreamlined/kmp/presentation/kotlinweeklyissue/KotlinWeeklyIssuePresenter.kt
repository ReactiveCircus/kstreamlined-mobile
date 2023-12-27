package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import io.github.reactivecircus.kstreamlined.kmp.core.utils.runCatchingNonCancellationException
import io.github.reactivecircus.kstreamlined.kmp.data.feed.FeedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public class KotlinWeeklyIssuePresenter(
    private val feedRepository: FeedRepository,
) {
    private val _uiState = MutableStateFlow<KotlinWeeklyIssueUiState>(
        KotlinWeeklyIssueUiState.InFlight
    )
    public val uiState: StateFlow<KotlinWeeklyIssueUiState> = _uiState.asStateFlow()

    public suspend fun loadKotlinWeeklyIssue(id: String) {
        _uiState.value = KotlinWeeklyIssueUiState.InFlight
        runCatchingNonCancellationException {
            val feedItem = feedRepository.loadFeedItemById(id) ?: error("Feed item not found")
            feedItem to feedRepository.loadKotlinWeeklyIssue(feedItem.contentUrl)
        }.fold(
            onSuccess = { (feedItem, kotlinWeeklyIssue) ->
                _uiState.value = KotlinWeeklyIssueUiState.Content(
                    issueItems = kotlinWeeklyIssue,
                    savedForLater = feedItem.savedForLater
                )
            },
            onFailure = {
                _uiState.value = KotlinWeeklyIssueUiState.Error
            }
        )
    }
}
