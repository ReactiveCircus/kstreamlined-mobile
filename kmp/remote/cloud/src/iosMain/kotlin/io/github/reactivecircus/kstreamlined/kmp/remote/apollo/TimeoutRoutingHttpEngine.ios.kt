package io.github.reactivecircus.kstreamlined.kmp.remote.apollo

import com.apollographql.apollo.network.http.DefaultHttpEngine
import com.apollographql.apollo.network.http.HttpEngine
import kotlin.time.Duration

/**
 * Creates an [HttpEngine] backed by `NSURLSession` with a request timeout of [defaultTimeout].
 *
 * Operations with a [requestTimeout] are executed with the request timeout instead.
 */
public fun createTimeoutRoutingHttpEngine(defaultTimeout: Duration): HttpEngine =
    TimeoutRoutingHttpEngine(
        defaultEngine = DefaultHttpEngine(timeoutMillis = defaultTimeout.inWholeMilliseconds),
        timeoutEngineFactory = { timeout ->
            DefaultHttpEngine(timeoutMillis = timeout.inWholeMilliseconds)
        },
    )
