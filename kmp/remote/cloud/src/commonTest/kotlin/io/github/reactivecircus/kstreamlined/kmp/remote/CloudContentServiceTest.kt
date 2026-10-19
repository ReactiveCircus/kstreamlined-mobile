package io.github.reactivecircus.kstreamlined.kmp.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.api.toResponseJson
import com.apollographql.apollo.exception.CacheMissException
import com.apollographql.apollo.exception.NoDataException
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.apollographql.cache.normalized.api.FieldPolicyCacheResolver
import com.apollographql.cache.normalized.api.TypePolicyCacheKeyGenerator
import com.apollographql.cache.normalized.memory.MemoryCacheFactory
import com.apollographql.cache.normalized.normalizedCache
import com.apollographql.mockserver.MockServer
import com.apollographql.mockserver.enqueueError
import com.apollographql.mockserver.enqueueString
import io.github.reactivecircus.kstreamlined.graphql.KotlinBlogTldrQuery
import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.graphql.builder.Data
import io.github.reactivecircus.kstreamlined.graphql.builder.buildKotlinBlogTldr
import io.github.reactivecircus.kstreamlined.graphql.builder.buildKotlinWeeklyIssueEntry
import io.github.reactivecircus.kstreamlined.graphql.builder.resolver.DefaultFakeResolver
import io.github.reactivecircus.kstreamlined.graphql.cache.Cache
import io.github.reactivecircus.kstreamlined.graphql.type.KotlinWeeklyIssueEntryGroup
import io.github.reactivecircus.kstreamlined.kmp.remote.apollo.RequestTimeout
import io.github.reactivecircus.kstreamlined.kmp.remote.mapper.asExternalModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class CloudContentServiceTest {
    private lateinit var mockServer: MockServer

    private lateinit var cloudContentService: CloudContentService

    private val httpRequests = mutableListOf<HttpRequest>()

    private val dummyKotlinWeeklyEntries = KotlinWeeklyIssueQuery.Data(resolver = DefaultFakeResolver()) {
        kotlinWeeklyIssue = listOf(
            buildKotlinWeeklyIssueEntry {
                title = "Kotlin Weekly entry 1"
                group = KotlinWeeklyIssueEntryGroup.NEWS
            },
            buildKotlinWeeklyIssueEntry {
                title = "Kotlin Weekly entry 2"
                group = KotlinWeeklyIssueEntryGroup.ARTICLES
            },
        )
    }.kotlinWeeklyIssueEntries

    private val dummyKotlinBlogTldr = KotlinBlogTldrQuery.Data(resolver = DefaultFakeResolver()) {
        kotlinBlogTldr = buildKotlinBlogTldr {
            id = "1"
            content = "TLDR content"
            model = "gpt-oss-120b"
            generatedAt = Instant.parse("2026-09-07T00:00:00Z")
        }
    }.kotlinBlogTldr

    @BeforeTest
    fun setUp() = runBlocking {
        mockServer = MockServer()
        cloudContentService = CloudContentService(
            apolloClient = ApolloClient.Builder()
                .serverUrl(mockServer.url())
                .addHttpInterceptor(
                    object : HttpInterceptor {
                        override suspend fun intercept(
                            request: HttpRequest,
                            chain: HttpInterceptorChain,
                        ): HttpResponse {
                            httpRequests += request
                            return chain.proceed(request)
                        }
                    },
                )
                .normalizedCache(
                    normalizedCacheFactory = MemoryCacheFactory(),
                    cacheKeyGenerator = TypePolicyCacheKeyGenerator(
                        typePolicies = Cache.typePolicies,
                    ),
                    cacheResolver = FieldPolicyCacheResolver(
                        fieldPolicies = Cache.fieldPolicies,
                    ),
                )
                .build(),
        )
    }

    @AfterTest
    fun tearDown() {
        mockServer.close()
    }

    @Test
    fun `fetchKotlinWeeklyIssue returns result when network request was successful`() = runTest {
        mockServer.enqueueString(
            KotlinWeeklyIssueQuery.Data(kotlinWeeklyIssueEntries = dummyKotlinWeeklyEntries)
                .toResponseJson(),
        )
        val actual = cloudContentService.fetchKotlinWeeklyIssue(url = "url")
        assertEquals(dummyKotlinWeeklyEntries.map { it.asExternalModel() }, actual)
        assertNull(httpRequests.single().executionContext[RequestTimeout])
    }

    @Test
    fun `fetchKotlinWeeklyIssue throws exception when network request failed and cache is missing`() =
        runTest {
            mockServer.enqueueError(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudContentService.fetchKotlinWeeklyIssue(url = "url")
            }
            assertTrue(exception.cause is CacheMissException)
        }

    @Test
    fun `fetchKotlinWeeklyIssue returns result when network request failed but cache is available`() =
        runTest {
            // 1st request to populate cache
            mockServer.enqueueString(
                KotlinWeeklyIssueQuery.Data(kotlinWeeklyIssueEntries = dummyKotlinWeeklyEntries)
                    .toResponseJson(),
            )
            cloudContentService.fetchKotlinWeeklyIssue(url = "url")

            // 2nd request to consume cache
            mockServer.enqueueError(statusCode = 404)
            val actual = cloudContentService.fetchKotlinWeeklyIssue(url = "url")
            assertEquals(dummyKotlinWeeklyEntries.map { it.asExternalModel() }, actual)
        }

    @Test
    fun `fetchKotlinBlogTldr returns result when network request was successful`() = runTest {
        mockServer.enqueueString(
            KotlinBlogTldrQuery.Data(kotlinBlogTldr = dummyKotlinBlogTldr)
                .toResponseJson(),
        )
        val actual = cloudContentService.fetchKotlinBlogTldr(id = "1")
        assertEquals(dummyKotlinBlogTldr.asExternalModel(), actual)
        assertEquals(
            RequestTimeout(30.seconds),
            httpRequests.single().executionContext[RequestTimeout],
        )
    }

    @Test
    fun `fetchKotlinBlogTldr throws exception when network request failed and cache is missing`() =
        runTest {
            mockServer.enqueueError(statusCode = 404)
            val exception = assertFailsWith<NoDataException> {
                cloudContentService.fetchKotlinBlogTldr(id = "1")
            }
            assertTrue(exception.cause is CacheMissException)
        }

    @Test
    fun `fetchKotlinBlogTldr returns result when network request failed but cache is available`() =
        runTest {
            // 1st request to populate cache
            mockServer.enqueueString(
                KotlinBlogTldrQuery.Data(kotlinBlogTldr = dummyKotlinBlogTldr)
                    .toResponseJson(),
            )
            cloudContentService.fetchKotlinBlogTldr(id = "1")

            // 2nd request to consume cache
            mockServer.enqueueError(statusCode = 404)
            val actual = cloudContentService.fetchKotlinBlogTldr(id = "1")
            assertEquals(dummyKotlinBlogTldr.asExternalModel(), actual)
        }
}
