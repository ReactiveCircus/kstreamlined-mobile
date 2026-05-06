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
    @Suppress("MagicNumber")
    @Immutable
    public enum class Group(
        public val sourceColorArgb: Long,
        public val onSourceColorArgb: Long,
    ) {
        Announcements(0xFF7874B4, 0xFFFFFFFF),
        Articles(0xFFF1646C, 0xFFFFFFFF),
        Android(0xFF79C5B4, 0xFFFFFFFF),
        Videos(0xFF639FCB, 0xFFFFFFFF),
        Libraries(0xFF800000, 0xFFFFFFFF),
    }
}
