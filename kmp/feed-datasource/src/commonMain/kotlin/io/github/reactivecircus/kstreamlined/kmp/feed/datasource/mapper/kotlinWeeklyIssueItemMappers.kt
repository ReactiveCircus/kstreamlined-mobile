package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry

internal fun KotlinWeeklyIssueEntry.asExternalModel(): KotlinWeeklyIssueItem {
    return KotlinWeeklyIssueItem(
        title = this.title,
        summary = this.summary,
        url = this.url,
        source = this.source,
        group = when (this.group) {
            Announcements -> Announcements
            Articles -> Articles
            Android -> Android
            Videos -> Videos
            Libraries -> Libraries
        },
    )
}
