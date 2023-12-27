package io.github.reactivecircus.kstreamlined.kmp.data.feed.mapper

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.KotlinWeeklyIssueEntry
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem

internal fun KotlinWeeklyIssueEntry.asExternalModel(): KotlinWeeklyIssueItem {
    return KotlinWeeklyIssueItem(
        title = this.title,
        summary = this.summary,
        url = this.url,
        source = this.source,
        type = when (this.type) {
            KotlinWeeklyIssueEntry.Type.Announcement -> KotlinWeeklyIssueItem.Type.Announcement
            KotlinWeeklyIssueEntry.Type.Article -> KotlinWeeklyIssueItem.Type.Article
            KotlinWeeklyIssueEntry.Type.Android -> KotlinWeeklyIssueItem.Type.Android
            KotlinWeeklyIssueEntry.Type.Video -> KotlinWeeklyIssueItem.Type.Video
            KotlinWeeklyIssueEntry.Type.Library -> KotlinWeeklyIssueItem.Type.Library
        },
    )
}
