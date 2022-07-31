package io.github.reactivecircus.kstreamlined.kmm.data.feed

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class FakeFeedRepoTest {

    private val fakeFeedRepo = FakeFeedRepo()

    @Test
    fun `returns expected response when nextFeedSourcesResponse succeeds`() = runTest {
        fakeFeedRepo.nextFeedSourcesResponse = {
            FakeFeedSources
        }
        assertEquals(
            FakeFeedSources,
            fakeFeedRepo.loadFeedSources(),
        )
    }

    @Test
    fun `throws exception when nextFeedSourcesResponse fails`() = runTest {
        fakeFeedRepo.nextFeedSourcesResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedRepo.loadFeedSources()
        }
    }

    @Test
    fun `returns expected response when nextFeedEntriesResponse succeeds`() = runTest {
        fakeFeedRepo.nextFeedEntriesResponse = {
            FakeFeedEntries
        }
        assertEquals(
            FakeFeedEntries,
            fakeFeedRepo.loadFeedEntries(),
        )
    }

    @Test
    fun `throws exception when nextFeedEntriesResponse fails`() = runTest {
        fakeFeedRepo.nextFeedEntriesResponse = { throw IllegalStateException() }
        assertFailsWith<IllegalStateException> {
            fakeFeedRepo.loadFeedEntries()
        }
    }
}
