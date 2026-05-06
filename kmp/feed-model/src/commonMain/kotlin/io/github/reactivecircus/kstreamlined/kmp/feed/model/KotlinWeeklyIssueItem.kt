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
        Announcements(0xFF_7874B4, 0xFF_FFFFFF),
        Articles(0xFF_F1646C, 0xFF_FFFFFF),
        Android(0xFF_79C5B4, 0xFF_FFFFFF),
        Videos(0xFF_639FCB, 0xFF_FFFFFF),
        Libraries(0xFF_800000, 0xFF_FFFFFF),
    }
}
