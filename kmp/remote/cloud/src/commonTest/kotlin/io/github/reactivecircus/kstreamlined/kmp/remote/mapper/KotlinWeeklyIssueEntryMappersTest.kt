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
            __typename = "KotlinWeeklyIssueEntry",
            title = "Title 1",
            summary = "Summary 1",
            url = "url 1",
            source = "Source 1",
            group = KotlinWeeklyIssueEntryGroup.NEWS,
        )
        val expected1 = KotlinWeeklyIssueEntry(
            title = "Title 1",
            summary = "Summary 1",
            url = "url 1",
            source = "Source 1",
            group = KotlinWeeklyIssueEntry.Group.News,
        )
        assertEquals(expected1, entry1.asExternalModel())

        val entry2 = KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry(
            __typename = "KotlinWeeklyIssueEntry",
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
            __typename = "KotlinWeeklyIssueEntry",
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
            __typename = "KotlinWeeklyIssueEntry",
            title = "Title 4",
            summary = "Summary 4",
            url = "url 4",
            source = "Source 4",
            group = KotlinWeeklyIssueEntryGroup.MULTIPLATFORM,
        )
        val expected4 = KotlinWeeklyIssueEntry(
            title = "Title 4",
            summary = "Summary 4",
            url = "url 4",
            source = "Source 4",
            group = KotlinWeeklyIssueEntry.Group.Multiplatform,
        )
        assertEquals(expected4, entry4.asExternalModel())

        val entry5 = KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry(
            __typename = "KotlinWeeklyIssueEntry",
            title = "Title 5",
            summary = "Summary 5",
            url = "url 5",
            source = "Source 5",
            group = KotlinWeeklyIssueEntryGroup.CONFERENCES,
        )
        val expected5 = KotlinWeeklyIssueEntry(
            title = "Title 5",
            summary = "Summary 5",
            url = "url 5",
            source = "Source 5",
            group = KotlinWeeklyIssueEntry.Group.Conferences,
        )
        assertEquals(expected5, entry5.asExternalModel())

        val entry6 = KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry(
            __typename = "KotlinWeeklyIssueEntry",
            title = "Title 6",
            summary = "Summary 6",
            url = "url 6",
            source = "Source 6",
            group = KotlinWeeklyIssueEntryGroup.VIDEOS,
        )
        val expected6 = KotlinWeeklyIssueEntry(
            title = "Title 6",
            summary = "Summary 6",
            url = "url 6",
            source = "Source 6",
            group = KotlinWeeklyIssueEntry.Group.Videos,
        )
        assertEquals(expected6, entry6.asExternalModel())

        val entry7 = KotlinWeeklyIssueQuery.KotlinWeeklyIssueEntry(
            __typename = "KotlinWeeklyIssueEntry",
            title = "Title 7",
            summary = "Summary 7",
            url = "url 7",
            source = "Source 7",
            group = KotlinWeeklyIssueEntryGroup.LIBRARIES,
        )
        val expected7 = KotlinWeeklyIssueEntry(
            title = "Title 7",
            summary = "Summary 7",
            url = "url 7",
            source = "Source 7",
            group = KotlinWeeklyIssueEntry.Group.Libraries,
        )
        assertEquals(expected7, entry7.asExternalModel())
    }
}
