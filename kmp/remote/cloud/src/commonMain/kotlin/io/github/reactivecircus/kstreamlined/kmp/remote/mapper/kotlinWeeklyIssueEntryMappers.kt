package io.github.reactivecircus.kstreamlined.kmp.remote.mapper

import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry

internal fun KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry.asExternalModel(): KotlinWeeklyIssueEntry? {
    return KotlinWeeklyIssueEntry(
        title = this.title,
        summary = this.summary,
        url = this.url,
        source = this.source,
        group = when (this.group) {
            ANNOUNCEMENTS -> KotlinWeeklyIssueEntry.Group.Announcements
            ARTICLES -> KotlinWeeklyIssueEntry.Group.Articles
            ANDROID -> KotlinWeeklyIssueEntry.Group.Android
            VIDEOS -> KotlinWeeklyIssueEntry.Group.Videos
            LIBRARIES -> KotlinWeeklyIssueEntry.Group.Libraries
            UNKNOWN__ -> return null
        },
    )
}
