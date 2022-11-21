package io.github.reactivecircus.kstreamlined.kmm.data.feed

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.exception.ApolloCompositeException
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.mockserver.MockResponse
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.testing.enqueue
import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.buildFeedSource
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.buildKotlinBlog
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.buildKotlinYouTube
import io.github.reactivecircus.kstreamlined.kmm.test.utils.runTest
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

    private suspend fun tearDown() {
        mockServer.stop()
    }

    @Test
    fun `loadFeedSources returns result when refresh = true and network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueue(
                FeedSourcesQuery(),
                FeedSourcesQuery.Data(feedSources = dummyFeedSources)
            )
            val actual = cloudFeedRepo.loadFeedSources(refresh = true)
            assertEquals(dummyFeedSources, actual)
        }

    @Test
    fun `loadFeedSources throws exception when refresh = true and network request failed`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueue(
                MockResponse.Builder().statusCode(statusCode = 404)
                    .headers(mapOf("Content-Length" to "0"))
                    .build()
            )
            val exception = assertFailsWith<ApolloHttpException> {
                cloudFeedRepo.loadFeedSources(refresh = true)
            }
            assertEquals(404, exception.statusCode)
        }

    @Test
    fun `loadFeedSources returns result when refresh = false and network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueue(
                FeedSourcesQuery(),
                FeedSourcesQuery.Data(feedSources = dummyFeedSources)
            )
            val actual = cloudFeedRepo.loadFeedSources(refresh = false)
            assertEquals(dummyFeedSources, actual)
        }

    @Test
    fun `loadFeedSources returns result when refresh = false and network request failed but cache is available`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            // 1st request to populate cache
            mockServer.enqueue(
                FeedSourcesQuery(),
                FeedSourcesQuery.Data(feedSources = dummyFeedSources)
            )
            cloudFeedRepo.loadFeedSources(refresh = false)

            // 2nd request to consume cache
            mockServer.enqueue(
                MockResponse.Builder().statusCode(statusCode = 404)
                    .headers(mapOf("Content-Length" to "0"))
                    .build()
            )
            val actual = cloudFeedRepo.loadFeedSources(refresh = false)
            assertEquals(dummyFeedSources, actual)
        }

    @Test
    fun `loadFeedSources throws exception when refresh = false and network request failed and cache is missing`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueue(
                MockResponse.Builder().statusCode(statusCode = 404)
                    .headers(mapOf("Content-Length" to "0"))
                    .build()
            )
            val exception = assertFailsWith<ApolloCompositeException> {
                cloudFeedRepo.loadFeedSources(refresh = false)
            }
            assertTrue(exception.suppressedExceptions[0] is CacheMissException)
            assertTrue(exception.suppressedExceptions[1] is ApolloHttpException)
        }

    @Test
    fun `loadFeedEntries returns result when refresh = true and network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueue(
                FeedEntriesQuery(Optional.Absent),
                FeedEntriesQuery.Data(feedEntries = dummyFeedEntries)
            )
            val actual = cloudFeedRepo.loadFeedEntries(
                filters = null,
                refresh = true,
            )
            assertEquals(dummyFeedEntries, actual)
        }

    @Test
    fun `loadFeedEntries throws exception when refresh = true and network request failed`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueue(
                MockResponse.Builder().statusCode(statusCode = 404)
                    .headers(mapOf("Content-Length" to "0"))
                    .build()
            )
            val exception = assertFailsWith<ApolloHttpException> {
                cloudFeedRepo.loadFeedEntries(
                    filters = null,
                    refresh = true,
                )
            }
            assertEquals(404, exception.statusCode)
        }

    @Test
    fun `loadFeedEntries returns result when refresh = false and network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueue(
                FeedEntriesQuery(Optional.Absent),
                FeedEntriesQuery.Data(feedEntries = dummyFeedEntries)
            )
            val actual = cloudFeedRepo.loadFeedEntries(
                filters = null,
                refresh = false,
            )
            assertEquals(dummyFeedEntries, actual)
        }

    @Test
    fun `loadFeedEntries returns result when refresh = false and network request failed but cache is available`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            // 1st request to populate cache
            mockServer.enqueue(
                FeedEntriesQuery(Optional.Absent),
                FeedEntriesQuery.Data(feedEntries = dummyFeedEntries)
            )
            cloudFeedRepo.loadFeedEntries(
                filters = null,
                refresh = false,
            )

            // 2nd request to consume cache
            mockServer.enqueue(
                MockResponse.Builder().statusCode(statusCode = 404)
                    .headers(mapOf("Content-Length" to "0"))
                    .build()
            )
            val actual = cloudFeedRepo.loadFeedEntries(
                filters = null,
                refresh = false,
            )
            assertEquals(dummyFeedEntries, actual)
        }

    @Test
    fun `loadFeedEntries throws exception when refresh = false and network request failed and cache is missing`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueue(
                MockResponse.Builder().statusCode(statusCode = 404)
                    .headers(mapOf("Content-Length" to "0"))
                    .build()
            )
            val exception = assertFailsWith<ApolloCompositeException> {
                cloudFeedRepo.loadFeedEntries(
                    filters = null,
                    refresh = false,
                )
            }
            assertTrue(exception.suppressedExceptions[0] is CacheMissException)
            assertTrue(exception.suppressedExceptions[1] is ApolloHttpException)
        }
}
