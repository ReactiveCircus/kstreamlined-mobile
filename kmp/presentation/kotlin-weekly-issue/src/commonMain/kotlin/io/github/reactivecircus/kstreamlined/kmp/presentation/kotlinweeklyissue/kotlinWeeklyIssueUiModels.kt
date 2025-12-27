package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import androidx.compose.runtime.Immutable
import io.github.reactivecircus.kstreamlined.kmp.feed.model.KotlinWeeklyIssueItem

@Immutable
public sealed interface KotlinWeeklyIssueUiState {
    public data object Loading : KotlinWeeklyIssueUiState

    public data object Error : KotlinWeeklyIssueUiState

    public data class Content(
        val id: String,
        val contentUrl: String,
        val issueItems: Map<KotlinWeeklyIssueItem.Group, List<KotlinWeeklyIssueItem>>,
        val savedForLater: Boolean,
    ) : KotlinWeeklyIssueUiState
}

public sealed interface KotlinWeeklyIssueUiEvent {
    public data object Refresh : KotlinWeeklyIssueUiEvent

    public data object ToggleSavedForLater : KotlinWeeklyIssueUiEvent
}
