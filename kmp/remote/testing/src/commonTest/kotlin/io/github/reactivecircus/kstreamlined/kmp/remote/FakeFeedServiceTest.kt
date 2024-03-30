package io.github.reactivecircus.kstreamlined.kmp.remote

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FakeFeedServiceTest {

    private val fakeFeedService = FakeFeedService()

    @Test
    fun `returns expected response when nextFeedSourcesResponse succeeds`() = runTest {
        fakeFeedService.nextFeedSourcesResponse = {
            FakeFeedSources
        }
        assertEquals(
            FakeFeedSources,
            fakeFeedService.fetchFeedOrigins(),
        )
    }

    @Test
    fun `throws exception when nextFeedSourcesResponse fails`() = runTest {
        fakeFeedService.nextFeedSourcesResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedService.fetchFeedOrigins()
        }
    }

    @Test
    fun `returns expected response when nextFeedEntriesResponse succeeds`() = runTest {
        fakeFeedService.nextFeedEntriesResponse = {
            FakeFeedEntries
        }
        assertEquals(
            FakeFeedEntries,
            fakeFeedService.fetchFeedEntries(),
        )
    }

    @Test
    fun `throws exception when nextFeedEntriesResponse fails`() = runTest {
        fakeFeedService.nextFeedEntriesResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedService.fetchFeedEntries()
        }
    }

    @Test
    fun `returns expected response when nextKotlinWeeklyIssueResponse succeeds`() = runTest {
        fakeFeedService.nextKotlinWeeklyIssueResponse = {
            FakeKotlinWeeklyIssueEntries
        }
        assertEquals(
            FakeKotlinWeeklyIssueEntries,
            fakeFeedService.fetchKotlinWeeklyIssue("url"),
        )
    }

    @Test
    fun `throws exception when nextKotlinWeeklyIssueResponse fails`() = runTest {
        fakeFeedService.nextKotlinWeeklyIssueResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedService.fetchKotlinWeeklyIssue("url")
        }
    }
}
