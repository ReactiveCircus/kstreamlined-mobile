package io.github.reactivecircus.kstreamlined.kmp.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.toResponseJson
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.NoDataException
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.mockserver.enqueueError
import com.apollographql.apollo3.mockserver.enqueueString
import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.graphql.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.graphql.type.KotlinWeeklyIssueEntryGroup
import io.github.reactivecircus.kstreamlined.graphql.type.buildFeedSource
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinBlog
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinWeeklyIssueEntry
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinYouTube
import io.github.reactivecircus.kstreamlined.kmp.remote.mapper.asExternalModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CloudFeedServiceTest {

    private lateinit var mockServer: MockServer

    private lateinit var cloudFeedService: CloudFeedService

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
                publishTime = Instant.parse("2023-11-16T11:59:46Z")
            },
            buildKotlinYouTube {
                title = "Kotlin YouTube entry"
                publishTime = Instant.parse("2023-11-21T18:47:47Z")
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

    @BeforeTest
    fun setUp() = runBlocking {
        mockServer = MockServer()
        cloudFeedService = CloudFeedService(
            apolloClient = ApolloClient.Builder()
                .serverUrl(mockServer.url())
                .normalizedCache(MemoryCacheFactory())
                .build()
        )
    }

    @AfterTest
    fun tearDown() {
        mockServer.close()
    }

    @Test
    fun `loadFeedSources returns result when network request was successful`() =
        runTest {
            mockServer.enqueueString(
                FeedSourcesQuery.Data(feedSources = dummyFeedSources).toResponseJson()
            )
            val actual = cloudFeedService.fetchFeedOrigins()
            assertEquals(dummyFeedSources.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `loadFeedSources throws exception when network request failed`() =
        runTest {
            mockServer.enqueueError(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedService.fetchFeedOrigins()
            }
            assertEquals(404, (exception.cause as ApolloHttpException).statusCode)
        }

    @Test
    fun `loadFeedEntries returns result when network request was successful`() =
        runTest {
            mockServer.enqueueString(
                FeedEntriesQuery.Data(feedEntries = dummyFeedEntries).toResponseJson()
            )
            val actual = cloudFeedService.fetchFeedEntries(filters = null)
            assertEquals(dummyFeedEntries.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `loadFeedEntries throws exception when network request failed`() =
        runTest {
            mockServer.enqueueError(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedService.fetchFeedEntries(filters = null)
            }
            assertEquals(404, (exception.cause as ApolloHttpException).statusCode)
        }

    @Test
    fun `loadKotlinWeeklyIssue returns result when network request was successful`() =
        runTest {
            mockServer.enqueueString(
                KotlinWeeklyIssueQuery.Data(kotlinWeeklyIssueEntries = dummyKotlinWeeklyEntries)
                    .toResponseJson()
            )
            val actual = cloudFeedService.fetchKotlinWeeklyIssue(url = "url")
            assertEquals(dummyKotlinWeeklyEntries.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `loadKotlinWeeklyIssue throws exception when network request failed and cache is missing`() =
        runTest {
            mockServer.enqueueError(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudFeedService.fetchKotlinWeeklyIssue(url = "url")
            }
            assertTrue(exception.cause is CacheMissException)
        }

    @Test
    fun `loadKotlinWeeklyIssue returns result when network request failed but cache is available`() =
        runTest {
            // 1st request to populate cache
            mockServer.enqueueString(
                KotlinWeeklyIssueQuery.Data(kotlinWeeklyIssueEntries = dummyKotlinWeeklyEntries)
                    .toResponseJson()
            )
            cloudFeedService.fetchKotlinWeeklyIssue(url = "url")

            // 2nd request to consume cache
            mockServer.enqueueError(statusCode = 404)
            val actual = cloudFeedService.fetchKotlinWeeklyIssue(url = "url")
            assertEquals(dummyKotlinWeeklyEntries.map { it.asExternalModel() }, actual)
        }
}
