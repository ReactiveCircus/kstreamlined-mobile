package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.kmp.feed.model.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry
import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinWeeklyIssueItemMappersTest {
    @Test
    fun `KotlinWeeklyIssueEntry maps to expected KotlinWeeklyIssueItem`() {
        val entry1 = KotlinWeeklyIssueEntry(
            title = "Title 1",
            summary = "Summary 1",
            url = "url 1",
            source = "Source 1",
            group = KotlinWeeklyIssueEntry.Group.Announcements,
        )
        val expected1 = KotlinWeeklyIssueItem(
            title = "Title 1",
            summary = "Summary 1",
            url = "url 1",
            source = "Source 1",
            group = KotlinWeeklyIssueItem.Group.Announcements,
        )
        assertEquals(expected1, entry1.asExternalModel())

        val entry2 = KotlinWeeklyIssueEntry(
            title = "Title 2",
            summary = "Summary 2",
            url = "url 2",
            source = "Source 2",
            group = KotlinWeeklyIssueEntry.Group.Articles,
        )
        val expected2 = KotlinWeeklyIssueItem(
            title = "Title 2",
            summary = "Summary 2",
            url = "url 2",
            source = "Source 2",
            group = KotlinWeeklyIssueItem.Group.Articles,
        )
        assertEquals(expected2, entry2.asExternalModel())

        val entry3 = KotlinWeeklyIssueEntry(
            title = "Title 3",
            summary = "Summary 3",
            url = "url 3",
            source = "Source 3",
            group = KotlinWeeklyIssueEntry.Group.Android,
        )
        val expected3 = KotlinWeeklyIssueItem(
            title = "Title 3",
            summary = "Summary 3",
            url = "url 3",
            source = "Source 3",
            group = KotlinWeeklyIssueItem.Group.Android,
        )
        assertEquals(expected3, entry3.asExternalModel())

        val entry4 = KotlinWeeklyIssueEntry(
            title = "Title 4",
            summary = "Summary 4",
            url = "url 4",
            source = "Source 4",
            group = KotlinWeeklyIssueEntry.Group.Videos,
        )
        val expected4 = KotlinWeeklyIssueItem(
            title = "Title 4",
            summary = "Summary 4",
            url = "url 4",
            source = "Source 4",
            group = KotlinWeeklyIssueItem.Group.Videos,
        )
        assertEquals(expected4, entry4.asExternalModel())

        val entry5 = KotlinWeeklyIssueEntry(
            title = "Title 5",
            summary = "Summary 5",
            url = "url 5",
            source = "Source 5",
            group = KotlinWeeklyIssueEntry.Group.Libraries,
        )
        val expected5 = KotlinWeeklyIssueItem(
            title = "Title 5",
            summary = "Summary 5",
            url = "url 5",
            source = "Source 5",
            group = KotlinWeeklyIssueItem.Group.Libraries,
        )
        assertEquals(expected5, entry5.asExternalModel())
    }
}
