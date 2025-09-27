package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem

public sealed interface KotlinWeeklyIssueUiState {
    public data object Loading : KotlinWeeklyIssueUiState

    public data object Failed : KotlinWeeklyIssueUiState

    public data class Content(
        val id: String,
        val contentUrl: String,
        val issueItems: Map<KotlinWeeklyIssueItem.Group, List<KotlinWeeklyIssueItem>>,
        val savedForLater: Boolean,
    ) : KotlinWeeklyIssueUiState
}

public sealed interface KotlinWeeklyIssueUiEvent {
    public data class LoadIssue(val id: String) : KotlinWeeklyIssueUiEvent

    public data object ToggleSavedForLater : KotlinWeeklyIssueUiEvent

    public data object Reset : KotlinWeeklyIssueUiEvent
}
