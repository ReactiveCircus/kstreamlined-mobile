package io.github.reactivecircus.kstreamlined.kmp.feed.model

import androidx.compose.runtime.Immutable

@Immutable
public data class KotlinWeeklyIssueItem(
    val title: String,
    val summary: String,
    val url: String,
    val source: String,
    val group: Group,
) {
    @Immutable
    public enum class Group {
        Announcements,
        Articles,
        Android,
        Videos,
        Libraries,
    }
}
