package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.graphql.type.KotlinWeeklyIssueEntryType
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.KotlinWeeklyIssueEntry

internal fun KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry.asExternalModel(): KotlinWeeklyIssueEntry? {
    return KotlinWeeklyIssueEntry(
        title = this.title,
        summary = this.summary,
        url = this.url,
        source = this.source,
        type = when (this.type) {
            KotlinWeeklyIssueEntryType.ANNOUNCEMENTS -> KotlinWeeklyIssueEntry.Type.Announcement
            KotlinWeeklyIssueEntryType.ARTICLES -> KotlinWeeklyIssueEntry.Type.Article
            KotlinWeeklyIssueEntryType.ANDROID -> KotlinWeeklyIssueEntry.Type.Android
            KotlinWeeklyIssueEntryType.VIDEOS -> KotlinWeeklyIssueEntry.Type.Video
            KotlinWeeklyIssueEntryType.LIBRARIES -> KotlinWeeklyIssueEntry.Type.Library
            KotlinWeeklyIssueEntryType.UNKNOWN__ -> return null
        },
    )
}
