package io.github.reactivecircus.kstreamlined.kmp.data.feed

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.NoDataException
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.mockserver.enqueueString
import com.apollographql.apollo3.testing.enqueueData
import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.graphql.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.graphql.type.buildFeedSource
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinBlog
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinYouTube
import io.github.reactivecircus.kstreamlined.kmp.data.feed.mapper.toModel
import io.github.reactivecircus.kstreamlined.kmp.test.utils.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@ApolloExperimental
class CloudFeedRepoTest {

    private lateinit var mockServer: MockServer

    private lateinit var cloudFeedRepo: CloudFeedRepo

    private val dummyFeedSources = FeedSourcesQuery.Data {
        feedSources = listOf(
            buildFeedSource {
                title = "Kotlin Blog"
            },
            buildFeedSource {
                title = "Kotlin YouTube"
            },
        )
    }.feedSources

    private val dummyFeedEntries = FeedEntriesQuery.Data {
        feedEntries = listOf(
            buildKotlinBlog {
                title = "Kotlin blog entry"
            },
            buildKotlinYouTube {
                title = "Kotlin YouTube entry"
            }
        )
    }.feedEntries

    private suspend fun setUp() {
        mockServer = MockServer()
        cloudFeedRepo = CloudFeedRepo(
            apolloClient = ApolloClient.Builder()
                .serverUrl(mockServer.url())
                .normalizedCache(MemoryCacheFactory())
                .build()
        )
    }

    private fun tearDown() {
        mockServer.close()
    }

    @Test
    fun `loadFeedSources returns result when refresh = true and network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueData(
                FeedSourcesQuery.Data(feedSources = dummyFeedSources)
            )
            val actual = cloudFeedRepo.loadFeedSources(refresh = true)
            assertEquals(dummyFeedSources.map { it.toModel() }, actual)
        }

    @Test
    fun `loadFeedSources throws exception when refresh = true and network request failed`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueString(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedRepo.loadFeedSources(refresh = true)
            }
            assertEquals(404, (exception.cause as ApolloHttpException).statusCode)
        }

    @Test
    fun `loadFeedSources returns result when refresh = false and network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueData(
                FeedSourcesQuery.Data(feedSources = dummyFeedSources)
            )
            val actual = cloudFeedRepo.loadFeedSources(refresh = false)
            assertEquals(dummyFeedSources.map { it.toModel() }, actual)
        }

    @Test
    fun `loadFeedSources returns result when refresh = false and network request failed but cache is available`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            // 1st request to populate cache
            mockServer.enqueueData(
                FeedSourcesQuery.Data(feedSources = dummyFeedSources)
            )
            cloudFeedRepo.loadFeedSources(refresh = false)

            // 2nd request to consume cache
            mockServer.enqueueString(statusCode = 404)
            val actual = cloudFeedRepo.loadFeedSources(refresh = false)
            assertEquals(dummyFeedSources.map { it.toModel() }, actual)
        }

    @Test
    fun `loadFeedSources throws exception when refresh = false and network request failed and cache is missing`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueString(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedRepo.loadFeedSources(refresh = false)
            }
            assertTrue(exception.cause is CacheMissException)
        }

    @Test
    fun `loadFeedEntries returns result when refresh = true and network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueData(
                FeedEntriesQuery.Data(feedEntries = dummyFeedEntries)
            )
            val actual = cloudFeedRepo.loadFeedEntries(
                filters = null,
                refresh = true,
            )
            assertEquals(dummyFeedEntries.map { it.toModel() }, actual)
        }

    @Test
    fun `loadFeedEntries throws exception when refresh = true and network request failed`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueString(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedRepo.loadFeedEntries(
                    filters = null,
                    refresh = true,
                )
            }
            assertEquals(404, (exception.cause as ApolloHttpException).statusCode)
        }

    @Test
    fun `loadFeedEntries returns result when refresh = false and network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueData(
                FeedEntriesQuery.Data(feedEntries = dummyFeedEntries)
            )
            val actual = cloudFeedRepo.loadFeedEntries(
                filters = null,
                refresh = false,
            )
            assertEquals(dummyFeedEntries.map { it.toModel() }, actual)
        }

    @Test
    fun `loadFeedEntries returns result when refresh = false and network request failed but cache is available`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            // 1st request to populate cache
            mockServer.enqueueData(
                FeedEntriesQuery.Data(feedEntries = dummyFeedEntries)
            )
            cloudFeedRepo.loadFeedEntries(
                filters = null,
                refresh = false,
            )

            // 2nd request to consume cache
            mockServer.enqueueString(statusCode = 404)
            val actual = cloudFeedRepo.loadFeedEntries(
                filters = null,
                refresh = false,
            )
            assertEquals(dummyFeedEntries.map { it.toModel() }, actual)
        }

    @Test
    fun `loadFeedEntries throws exception when refresh = false and network request failed and cache is missing`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueString(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedRepo.loadFeedEntries(
                    filters = null,
                    refresh = false,
                )
            }
            assertTrue(exception.cause is CacheMissException)
        }
}
