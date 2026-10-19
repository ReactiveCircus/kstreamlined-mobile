package io.github.reactivecircus.kstreamlined.kmp.remote

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FakeContentServiceTest {
    private val fakeContentService = FakeContentService()

    @Test
    fun `returns expected response when fetchKotlinWeeklyIssue succeeds`() = runTest {
        fakeContentService.nextKotlinWeeklyIssueResponse = {
            FakeKotlinWeeklyIssueEntries
        }
        assertEquals(
            FakeKotlinWeeklyIssueEntries,
            fakeContentService.fetchKotlinWeeklyIssue("url"),
        )
    }

    @Test
    fun `throws exception when fetchKotlinWeeklyIssue fails`() = runTest {
        fakeContentService.nextKotlinWeeklyIssueResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeContentService.fetchKotlinWeeklyIssue("url")
        }
    }

    @Test
    fun `returns expected response when fetchKotlinBlogTldr succeeds`() = runTest {
        fakeContentService.nextKotlinBlogTldrResponse = {
            FakeKotlinBlogTldr
        }
        assertEquals(
            FakeKotlinBlogTldr,
            fakeContentService.fetchKotlinBlogTldr("id"),
        )
    }

    @Test
    fun `throws exception when fetchKotlinBlogTldr fails`() = runTest {
        fakeContentService.nextKotlinBlogTldrResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeContentService.fetchKotlinBlogTldr("id")
        }
    }
}
