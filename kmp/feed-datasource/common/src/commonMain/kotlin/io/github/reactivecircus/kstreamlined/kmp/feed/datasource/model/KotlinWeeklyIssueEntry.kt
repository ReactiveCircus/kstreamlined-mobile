package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model

public data class KotlinWeeklyIssueEntry(
    val title: String,
    val summary: String,
    val url: String,
    val source: String,
    val type: Type,
) {
    public enum class Type {
        Announcement,
        Article,
        Android,
        Video,
        Library,
    }
}
