package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry
import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinWeeklyIssueItemMapperTest {
    @Test
    fun `KotlinWeeklyIssueEntry maps to expected KotlinWeeklyIssueItem`() {
        val entry1 = KotlinWeeklyIssueEntry(
            title = "Title 1",
            summary = "Summary 1",
            url = "url 1",
            source = "Source 1",
            group = KotlinWeeklyIssueEntry.Group.News,
        )
        val expected1 = KotlinWeeklyIssueItem(
            title = "Title 1",
            summary = "Summary 1",
            url = "url 1",
            source = "Source 1",
            group = KotlinWeeklyIssueItem.Group.News,
        )
        assertEquals(expected1, entry1.asPresentationModel())

        val entry2 = KotlinWeeklyIssueEntry(
            title = "Title 2",
            summary = "Summary 2",
            url = "url 2",
            source = "Source 2",
            group = KotlinWeeklyIssueEntry.Group.Announcements,
        )
        val expected2 = KotlinWeeklyIssueItem(
            title = "Title 2",
            summary = "Summary 2",
            url = "url 2",
            source = "Source 2",
            group = KotlinWeeklyIssueItem.Group.Announcements,
        )
        assertEquals(expected2, entry2.asPresentationModel())

        val entry3 = KotlinWeeklyIssueEntry(
            title = "Title 3",
            summary = "Summary 3",
            url = "url 3",
            source = "Source 3",
            group = KotlinWeeklyIssueEntry.Group.Articles,
        )
        val expected3 = KotlinWeeklyIssueItem(
            title = "Title 3",
            summary = "Summary 3",
            url = "url 3",
            source = "Source 3",
            group = KotlinWeeklyIssueItem.Group.Articles,
        )
        assertEquals(expected3, entry3.asPresentationModel())

        val entry4 = KotlinWeeklyIssueEntry(
            title = "Title 4",
            summary = "Summary 4",
            url = "url 4",
            source = "Source 4",
            group = KotlinWeeklyIssueEntry.Group.Android,
        )
        val expected4 = KotlinWeeklyIssueItem(
            title = "Title 4",
            summary = "Summary 4",
            url = "url 4",
            source = "Source 4",
            group = KotlinWeeklyIssueItem.Group.Android,
        )
        assertEquals(expected4, entry4.asPresentationModel())

        val entry5 = KotlinWeeklyIssueEntry(
            title = "Title 5",
            summary = "Summary 5",
            url = "url 5",
            source = "Source 5",
            group = KotlinWeeklyIssueEntry.Group.Multiplatform,
        )
        val expected5 = KotlinWeeklyIssueItem(
            title = "Title 5",
            summary = "Summary 5",
            url = "url 5",
            source = "Source 5",
            group = KotlinWeeklyIssueItem.Group.Multiplatform,
        )
        assertEquals(expected5, entry5.asPresentationModel())

        val entry6 = KotlinWeeklyIssueEntry(
            title = "Title 6",
            summary = "Summary 6",
            url = "url 6",
            source = "Source 6",
            group = KotlinWeeklyIssueEntry.Group.Conferences,
        )
        val expected6 = KotlinWeeklyIssueItem(
            title = "Title 6",
            summary = "Summary 6",
            url = "url 6",
            source = "Source 6",
            group = KotlinWeeklyIssueItem.Group.Conferences,
        )
        assertEquals(expected6, entry6.asPresentationModel())

        val entry7 = KotlinWeeklyIssueEntry(
            title = "Title 7",
            summary = "Summary 7",
            url = "url 7",
            source = "Source 7",
            group = KotlinWeeklyIssueEntry.Group.Videos,
        )
        val expected7 = KotlinWeeklyIssueItem(
            title = "Title 7",
            summary = "Summary 7",
            url = "url 7",
            source = "Source 7",
            group = KotlinWeeklyIssueItem.Group.Videos,
        )
        assertEquals(expected7, entry7.asPresentationModel())

        val entry8 = KotlinWeeklyIssueEntry(
            title = "Title 8",
            summary = "Summary 8",
            url = "url 8",
            source = "Source 8",
            group = KotlinWeeklyIssueEntry.Group.Libraries,
        )
        val expected8 = KotlinWeeklyIssueItem(
            title = "Title 8",
            summary = "Summary 8",
            url = "url 8",
            source = "Source 8",
            group = KotlinWeeklyIssueItem.Group.Libraries,
        )
        assertEquals(expected8, entry8.asPresentationModel())
    }
}
