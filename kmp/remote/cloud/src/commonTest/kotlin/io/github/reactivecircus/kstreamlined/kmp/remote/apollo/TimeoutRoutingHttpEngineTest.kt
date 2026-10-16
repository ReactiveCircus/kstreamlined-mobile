package io.github.reactivecircus.kstreamlined.kmp.remote.apollo

import com.apollographql.apollo.api.http.HttpMethod
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpEngine
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TimeoutRoutingHttpEngineTest {
    private val defaultEngine = RecordingHttpEngine()

    private val timeoutEngines = mutableMapOf<Duration, MutableList<RecordingHttpEngine>>()

    private val engine = TimeoutRoutingHttpEngine(
        defaultEngine = defaultEngine,
        timeoutEngineFactory = { timeout ->
            RecordingHttpEngine().also { timeoutEngines.getOrPut(timeout) { mutableListOf() } += it }
        },
    )

    @Test
    fun `request without RequestTimeout is executed by defaultEngine`() = runTest {
        val request = buildRequest(timeout = null)
        engine.execute(request)
        assertEquals(listOf(request), defaultEngine.requests)
        assertTrue(timeoutEngines.isEmpty())
    }

    @Test
    fun `request with RequestTimeout is executed by engine created for the timeout`() = runTest {
        val request = buildRequest(timeout = 30.seconds)
        engine.execute(request)
        assertEquals(listOf(request), timeoutEngines.getValue(30.seconds).single().requests)
        assertTrue(defaultEngine.requests.isEmpty())
    }

    @Test
    fun `requests with the same RequestTimeout reuse the same engine`() = runTest {
        val request1 = buildRequest(timeout = 30.seconds)
        val request2 = buildRequest(timeout = 30.seconds)
        engine.execute(request1)
        engine.execute(request2)
        assertEquals(listOf(request1, request2), timeoutEngines.getValue(30.seconds).single().requests)
    }

    @Test
    fun `requests with different RequestTimeouts are executed by different engines`() = runTest {
        val request1 = buildRequest(timeout = 20.seconds)
        val request2 = buildRequest(timeout = 30.seconds)
        engine.execute(request1)
        engine.execute(request2)
        assertEquals(listOf(request1), timeoutEngines.getValue(20.seconds).single().requests)
        assertEquals(listOf(request2), timeoutEngines.getValue(30.seconds).single().requests)
    }

    @Test
    fun `close closes defaultEngine and all created engines`() = runTest {
        engine.execute(buildRequest(timeout = 20.seconds))
        engine.execute(buildRequest(timeout = 30.seconds))
        engine.close()
        assertTrue(defaultEngine.closed)
        assertTrue(timeoutEngines.values.flatten().all { it.closed })
    }

    @Test
    fun `RequestTimeout throws exception when duration is not positive and finite`() {
        assertFailsWith<IllegalArgumentException> { RequestTimeout(Duration.ZERO) }
        assertFailsWith<IllegalArgumentException> { RequestTimeout(Duration.INFINITE) }
    }

    private fun buildRequest(timeout: Duration?): HttpRequest =
        HttpRequest.Builder(HttpMethod.Post, "url")
            .apply {
                if (timeout != null) addExecutionContext(RequestTimeout(timeout))
            }
            .build()
}

private class RecordingHttpEngine : HttpEngine {
    val requests = mutableListOf<HttpRequest>()

    var closed = false

    override suspend fun execute(request: HttpRequest): HttpResponse {
        requests += request
        return HttpResponse.Builder(statusCode = 200).build()
    }

    override fun close() {
        closed = true
    }
}
