package io.github.reactivecircus.kstreamlined.kmp.remote.mapper

import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.graphql.type.KotlinWeeklyIssueEntryGroup
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry
import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinWeeklyIssueEntryMappersTest {

    @Test
    fun `KotlinWeeklyIssueQuery_KotlinWeeklyIssueEntry maps to expected KotlinWeeklyIssueEntry`() {
        val entry1 = KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry(
            title = "Title 1",
            summary = "Summary 1",
            url = "url 1",
            source = "Source 1",
            group = KotlinWeeklyIssueEntryGroup.ANNOUNCEMENTS,
        )
        val expected1 = KotlinWeeklyIssueEntry(
            title = "Title 1",
            summary = "Summary 1",
            url = "url 1",
            source = "Source 1",
            group = KotlinWeeklyIssueEntry.Group.Announcements,
        )
        assertEquals(expected1, entry1.asExternalModel())

        val entry2 = KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry(
            title = "Title 2",
            summary = "Summary 2",
            url = "url 2",
            source = "Source 2",
            group = KotlinWeeklyIssueEntryGroup.ARTICLES,
        )
        val expected2 = KotlinWeeklyIssueEntry(
            title = "Title 2",
            summary = "Summary 2",
            url = "url 2",
            source = "Source 2",
            group = KotlinWeeklyIssueEntry.Group.Articles,
        )
        assertEquals(expected2, entry2.asExternalModel())

        val entry3 = KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry(
            title = "Title 3",
            summary = "Summary 3",
            url = "url 3",
            source = "Source 3",
            group = KotlinWeeklyIssueEntryGroup.ANDROID,
        )
        val expected3 = KotlinWeeklyIssueEntry(
            title = "Title 3",
            summary = "Summary 3",
            url = "url 3",
            source = "Source 3",
            group = KotlinWeeklyIssueEntry.Group.Android,
        )
        assertEquals(expected3, entry3.asExternalModel())

        val entry4 = KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry(
            title = "Title 4",
            summary = "Summary 4",
            url = "url 4",
            source = "Source 4",
            group = KotlinWeeklyIssueEntryGroup.VIDEOS,
        )
        val expected4 = KotlinWeeklyIssueEntry(
            title = "Title 4",
            summary = "Summary 4",
            url = "url 4",
            source = "Source 4",
            group = KotlinWeeklyIssueEntry.Group.Videos,
        )
        assertEquals(expected4, entry4.asExternalModel())

        val entry5 = KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry(
            title = "Title 5",
            summary = "Summary 5",
            url = "url 5",
            source = "Source 5",
            group = KotlinWeeklyIssueEntryGroup.LIBRARIES,
        )
        val expected5 = KotlinWeeklyIssueEntry(
            title = "Title 5",
            summary = "Summary 5",
            url = "url 5",
            source = "Source 5",
            group = KotlinWeeklyIssueEntry.Group.Libraries,
        )
        assertEquals(expected5, entry5.asExternalModel())
    }
}
