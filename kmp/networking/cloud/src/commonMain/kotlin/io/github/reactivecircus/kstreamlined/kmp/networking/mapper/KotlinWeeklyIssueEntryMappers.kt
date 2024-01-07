package io.github.reactivecircus.kstreamlined.kmp.networking.mapper

import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.graphql.type.KotlinWeeklyIssueEntryGroup
import io.github.reactivecircus.kstreamlined.kmp.networking.model.KotlinWeeklyIssueEntry

internal fun KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry.asExternalModel(): KotlinWeeklyIssueEntry? {
    return KotlinWeeklyIssueEntry(
        title = this.title,
        summary = this.summary,
        url = this.url,
        source = this.source,
        group = when (this.group) {
            KotlinWeeklyIssueEntryGroup.ANNOUNCEMENTS -> KotlinWeeklyIssueEntry.Group.Announcements
            KotlinWeeklyIssueEntryGroup.ARTICLES -> KotlinWeeklyIssueEntry.Group.Articles
            KotlinWeeklyIssueEntryGroup.ANDROID -> KotlinWeeklyIssueEntry.Group.Android
            KotlinWeeklyIssueEntryGroup.VIDEOS -> KotlinWeeklyIssueEntry.Group.Videos
            KotlinWeeklyIssueEntryGroup.LIBRARIES -> KotlinWeeklyIssueEntry.Group.Libraries
            KotlinWeeklyIssueEntryGroup.UNKNOWN__ -> return null
        },
    )
}
