package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model

public data class KotlinWeeklyIssueEntry(
    val title: String,
    val summary: String,
    val url: String,
    val source: String,
    val group: Group,
) {
    public enum class Group {
        Announcements,
        Articles,
        Android,
        Videos,
        Libraries,
    }
}
