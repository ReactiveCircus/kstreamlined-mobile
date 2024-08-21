package io.github.reactivecircus.kstreamlined.kmp.remote

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FakeFeedServiceTest {

    private val fakeFeedService = FakeFeedService()

    @Test
    fun `returns expected response when fetchFeedOrigins succeeds`() = runTest {
        fakeFeedService.nextFeedSourcesResponse = {
            FakeFeedSources
        }
        assertEquals(
            FakeFeedSources,
            fakeFeedService.fetchFeedOrigins(),
        )
    }

    @Test
    fun `throws exception when fetchFeedOrigins fails`() = runTest {
        fakeFeedService.nextFeedSourcesResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedService.fetchFeedOrigins()
        }
    }

    @Test
    fun `returns expected response when fetchFeedEntries succeeds`() = runTest {
        fakeFeedService.nextFeedEntriesResponse = {
            FakeFeedEntries
        }
        assertEquals(
            FakeFeedEntries,
            fakeFeedService.fetchFeedEntries(),
        )
    }

    @Test
    fun `throws exception when fetchFeedEntries fails`() = runTest {
        fakeFeedService.nextFeedEntriesResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedService.fetchFeedEntries()
        }
    }

    @Test
    fun `returns expected response when fetchFeedEntriesAndOrigins succeeds`() = runTest {
        fakeFeedService.nextFeedEntriesAndOriginsResponse = {
            FakeFeedEntries to FakeFeedSources
        }
        assertEquals(
            FakeFeedEntries to FakeFeedSources,
            fakeFeedService.fetchFeedEntriesAndOrigins(),
        )
    }

    @Test
    fun `throws exception when fetchFeedEntriesAndOrigins fails`() = runTest {
        fakeFeedService.nextFeedEntriesAndOriginsResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedService.fetchFeedEntriesAndOrigins()
        }
    }

    @Test
    fun `returns expected response when fetchKotlinWeeklyIssue succeeds`() = runTest {
        fakeFeedService.nextKotlinWeeklyIssueResponse = {
            FakeKotlinWeeklyIssueEntries
        }
        assertEquals(
            FakeKotlinWeeklyIssueEntries,
            fakeFeedService.fetchKotlinWeeklyIssue("url"),
        )
    }

    @Test
    fun `throws exception when fetchKotlinWeeklyIssue fails`() = runTest {
        fakeFeedService.nextKotlinWeeklyIssueResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedService.fetchKotlinWeeklyIssue("url")
        }
    }
}
