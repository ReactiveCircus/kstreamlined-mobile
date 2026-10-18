package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry

internal fun KotlinWeeklyIssueEntry.asPresentationModel(): KotlinWeeklyIssueItem {
    return KotlinWeeklyIssueItem(
        title = this.title,
        summary = this.summary,
        url = this.url,
        source = this.source,
        group = when (this.group) {
            KotlinWeeklyIssueEntry.Group.News -> KotlinWeeklyIssueItem.Group.News
            KotlinWeeklyIssueEntry.Group.Announcements -> KotlinWeeklyIssueItem.Group.Announcements
            KotlinWeeklyIssueEntry.Group.Articles -> KotlinWeeklyIssueItem.Group.Articles
            KotlinWeeklyIssueEntry.Group.Android -> KotlinWeeklyIssueItem.Group.Android
            KotlinWeeklyIssueEntry.Group.Videos -> KotlinWeeklyIssueItem.Group.Videos
            KotlinWeeklyIssueEntry.Group.Multiplatform -> KotlinWeeklyIssueItem.Group.Multiplatform
            KotlinWeeklyIssueEntry.Group.Conferences -> KotlinWeeklyIssueItem.Group.Conferences
            KotlinWeeklyIssueEntry.Group.Libraries -> KotlinWeeklyIssueItem.Group.Libraries
        },
    )
}
