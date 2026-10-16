package io.github.reactivecircus.kstreamlined.kmp.remote.apollo

import com.apollographql.apollo.api.http.HttpMethod
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.network.http.HttpEngine
import com.apollographql.mockserver.MockServer
import com.apollographql.mockserver.enqueueString
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class PlatformTimeoutRoutingHttpEngineTest {
    private lateinit var mockServer: MockServer

    private val engines = mutableListOf<HttpEngine>()

    @BeforeTest
    fun setUp() {
        mockServer = MockServer()
    }

    @AfterTest
    fun tearDown() {
        engines.forEach { it.close() }
        mockServer.close()
    }

    @Test
    fun `request without RequestTimeout fails when response takes longer than default timeout`() = runTest {
        val engine = createEngine(defaultTimeout = ShortTimeout)
        mockServer.enqueueString(delayMs = ResponseDelay.inWholeMilliseconds)
        assertFailsWith<ApolloNetworkException> {
            engine.execute(buildRequest(timeout = null))
        }
    }

    @Test
    fun `request with longer RequestTimeout succeeds when response takes longer than default timeout`() = runTest {
        val engine = createEngine(defaultTimeout = ShortTimeout)
        mockServer.enqueueString(string = "ok", delayMs = ResponseDelay.inWholeMilliseconds)
        val response = engine.execute(buildRequest(timeout = LongTimeout))
        assertEquals(200, response.statusCode)
        assertEquals("ok", response.body?.readUtf8())
    }

    @Test
    fun `request with shorter RequestTimeout fails when response takes longer than request timeout`() = runTest {
        val engine = createEngine(defaultTimeout = LongTimeout)
        mockServer.enqueueString(delayMs = ResponseDelay.inWholeMilliseconds)
        assertFailsWith<ApolloNetworkException> {
            engine.execute(buildRequest(timeout = ShortTimeout))
        }
    }

    private fun createEngine(defaultTimeout: Duration): HttpEngine =
        platformTimeoutRoutingHttpEngine(defaultTimeout).also { engines += it }

    private suspend fun buildRequest(timeout: Duration?): HttpRequest =
        HttpRequest.Builder(HttpMethod.Get, mockServer.url())
            .apply {
                if (timeout != null) addExecutionContext(RequestTimeout(timeout))
            }
            .build()
}

private val ShortTimeout = 200.milliseconds
private val ResponseDelay = 1.seconds
private val LongTimeout = 5.seconds

/**
 * Creates the platform-specific [TimeoutRoutingHttpEngine] with a default timeout of [defaultTimeout].
 */
expect fun platformTimeoutRoutingHttpEngine(defaultTimeout: Duration): HttpEngine
