package io.github.reactivecircus.kstreamlined.kmp.model.feed

public data class KotlinWeeklyIssueItem(
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
