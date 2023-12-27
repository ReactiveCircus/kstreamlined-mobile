package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem

public sealed interface KotlinWeeklyIssueUiState {
    public data object InFlight : KotlinWeeklyIssueUiState
    public data object Error : KotlinWeeklyIssueUiState
    public data class Content(
        val issueItems: List<KotlinWeeklyIssueItem>,
        val savedForLater: Boolean,
    ) : KotlinWeeklyIssueUiState
}
