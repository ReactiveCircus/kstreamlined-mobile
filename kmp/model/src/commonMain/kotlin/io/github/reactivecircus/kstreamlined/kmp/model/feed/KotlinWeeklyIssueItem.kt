package io.github.reactivecircus.kstreamlined.kmp.model.feed

public data class KotlinWeeklyIssueItem(
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
