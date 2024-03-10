package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.networking.model.KotlinWeeklyIssueEntry

internal fun KotlinWeeklyIssueEntry.asExternalModel(): KotlinWeeklyIssueItem {
    return KotlinWeeklyIssueItem(
        title = this.title,
        summary = this.summary,
        url = this.url,
        source = this.source,
        group = when (this.group) {
            KotlinWeeklyIssueEntry.Group.Announcements -> KotlinWeeklyIssueItem.Group.Announcements
            KotlinWeeklyIssueEntry.Group.Articles -> KotlinWeeklyIssueItem.Group.Articles
            KotlinWeeklyIssueEntry.Group.Android -> KotlinWeeklyIssueItem.Group.Android
            KotlinWeeklyIssueEntry.Group.Videos -> KotlinWeeklyIssueItem.Group.Videos
            KotlinWeeklyIssueEntry.Group.Libraries -> KotlinWeeklyIssueItem.Group.Libraries
        },
    )
}
