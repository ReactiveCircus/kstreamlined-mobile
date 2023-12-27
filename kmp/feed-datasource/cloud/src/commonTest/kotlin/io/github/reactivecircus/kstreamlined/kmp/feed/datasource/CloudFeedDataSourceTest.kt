package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import com.apollographql.apollo3.ApolloClient
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
import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.graphql.type.KotlinWeeklyIssueEntryGroup
import io.github.reactivecircus.kstreamlined.graphql.type.buildFeedSource
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinBlog
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinWeeklyIssueEntry
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinYouTube
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.test.utils.runTest
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CloudFeedDataSourceTest {

    private lateinit var mockServer: MockServer

    private lateinit var cloudFeedDataSource: CloudFeedDataSource

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
                publishTime = "2023-11-16T11:59:46Z".toInstant()
            },
            buildKotlinYouTube {
                title = "Kotlin YouTube entry"
                publishTime = "2023-11-21T18:47:47Z".toInstant()
            }
        )
    }.feedEntries

    private val dummyKotlinWeeklyEntries = KotlinWeeklyIssueQuery.Data {
        kotlinWeeklyIssue = listOf(
            buildKotlinWeeklyIssueEntry {
                title = "Kotlin Weekly entry 1"
                group = KotlinWeeklyIssueEntryGroup.ANNOUNCEMENTS
            },
            buildKotlinWeeklyIssueEntry {
                title = "Kotlin Weekly entry 2"
                group = KotlinWeeklyIssueEntryGroup.ARTICLES
            }
        )
    }.kotlinWeeklyIssueEntries

    private suspend fun setUp() {
        mockServer = MockServer()
        cloudFeedDataSource = CloudFeedDataSource(
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
            val actual = cloudFeedDataSource.loadFeedOrigins(refresh = true)
            assertEquals(dummyFeedSources.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `loadFeedSources throws exception when refresh = true and network request failed`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueString(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedDataSource.loadFeedOrigins(refresh = true)
            }
            assertEquals(404, (exception.cause as ApolloHttpException).statusCode)
        }

    @Test
    fun `loadFeedSources returns result when refresh = false and network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueData(
                FeedSourcesQuery.Data(feedSources = dummyFeedSources)
            )
            val actual = cloudFeedDataSource.loadFeedOrigins(refresh = false)
            assertEquals(dummyFeedSources.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `loadFeedSources returns result when refresh = false and network request failed but cache is available`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            // 1st request to populate cache
            mockServer.enqueueData(
                FeedSourcesQuery.Data(feedSources = dummyFeedSources)
            )
            cloudFeedDataSource.loadFeedOrigins(refresh = false)

            // 2nd request to consume cache
            mockServer.enqueueString(statusCode = 404)
            val actual = cloudFeedDataSource.loadFeedOrigins(refresh = false)
            assertEquals(dummyFeedSources.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `loadFeedSources throws exception when refresh = false and network request failed and cache is missing`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueString(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedDataSource.loadFeedOrigins(refresh = false)
            }
            assertTrue(exception.cause is CacheMissException)
        }

    @Test
    fun `loadFeedEntries returns result when refresh = true and network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueData(
                FeedEntriesQuery.Data(feedEntries = dummyFeedEntries)
            )
            val actual = cloudFeedDataSource.loadFeedEntries(
                filters = null,
                refresh = true,
            )
            assertEquals(dummyFeedEntries.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `loadFeedEntries throws exception when refresh = true and network request failed`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueString(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedDataSource.loadFeedEntries(
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
            val actual = cloudFeedDataSource.loadFeedEntries(
                filters = null,
                refresh = false,
            )
            assertEquals(dummyFeedEntries.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `loadFeedEntries returns result when refresh = false and network request failed but cache is available`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            // 1st request to populate cache
            mockServer.enqueueData(
                FeedEntriesQuery.Data(feedEntries = dummyFeedEntries)
            )
            cloudFeedDataSource.loadFeedEntries(
                filters = null,
                refresh = false,
            )

            // 2nd request to consume cache
            mockServer.enqueueString(statusCode = 404)
            val actual = cloudFeedDataSource.loadFeedEntries(
                filters = null,
                refresh = false,
            )
            assertEquals(dummyFeedEntries.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `loadFeedEntries throws exception when refresh = false and network request failed and cache is missing`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueString(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedDataSource.loadFeedEntries(
                    filters = null,
                    refresh = false,
                )
            }
            assertTrue(exception.cause is CacheMissException)
        }

    @Test
    fun `loadKotlinWeeklyIssue returns result when network request was successful`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueData(
                KotlinWeeklyIssueQuery.Data(kotlinWeeklyIssueEntries = dummyKotlinWeeklyEntries)
            )
            val actual = cloudFeedDataSource.loadKotlinWeeklyIssue(url = "url")
            assertEquals(dummyKotlinWeeklyEntries.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `loadKotlinWeeklyIssue throws exception when network request failed`() =
        runTest(before = { setUp() }, after = { tearDown() }) {
            mockServer.enqueueString(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedDataSource.loadKotlinWeeklyIssue(url = "url")
            }
            val apolloHttpException = exception.cause?.suppressedExceptions
                ?.first { it is ApolloHttpException } as ApolloHttpException
            assertEquals(404, apolloHttpException.statusCode)
        }
}
