package io.github.reactivecircus.kstreamlined.kmp.remote.apollo

import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpEngine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration

/**
 * An [HttpEngine] that executes requests with a [RequestTimeout] using an engine created by
 * [timeoutEngineFactory] for that timeout, and all other requests using [defaultEngine].
 *
 * Engines are created lazily and cached per timeout.
 */
internal class TimeoutRoutingHttpEngine(
    private val defaultEngine: HttpEngine,
    private val timeoutEngineFactory: (Duration) -> HttpEngine,
) : HttpEngine {
    private val mutex = Mutex()

    private val timeoutEngines = mutableMapOf<Duration, HttpEngine>()

    override suspend fun execute(request: HttpRequest): HttpResponse {
        val timeout = request.executionContext[RequestTimeout]?.duration
        val engine = if (timeout == null) {
            defaultEngine
        } else {
            mutex.withLock {
                timeoutEngines.getOrPut(timeout) { timeoutEngineFactory(timeout) }
            }
        }
        return engine.execute(request)
    }

    override fun close() {
        defaultEngine.close()
        timeoutEngines.values.toList().forEach { it.close() }
    }
}
