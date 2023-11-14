package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FakeFeedDataSourceTest {

    private val fakeFeedDataSource = FakeFeedDataSource()

    @Test
    fun `returns expected response when nextFeedSourcesResponse succeeds`() = runTest {
        fakeFeedDataSource.nextFeedSourcesResponse = {
            FakeFeedOrigins
        }
        assertEquals(
            FakeFeedOrigins,
            fakeFeedDataSource.loadFeedOrigins(),
        )
    }

    @Test
    fun `throws exception when nextFeedSourcesResponse fails`() = runTest {
        fakeFeedDataSource.nextFeedSourcesResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedDataSource.loadFeedOrigins()
        }
    }

    @Test
    fun `returns expected response when nextFeedEntriesResponse succeeds`() = runTest {
        fakeFeedDataSource.nextFeedEntriesResponse = {
            FakeFeedEntries
        }
        assertEquals(
            FakeFeedEntries,
            fakeFeedDataSource.loadFeedEntries(),
        )
    }

    @Test
    fun `throws exception when nextFeedEntriesResponse fails`() = runTest {
        fakeFeedDataSource.nextFeedEntriesResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedDataSource.loadFeedEntries()
        }
    }
}
