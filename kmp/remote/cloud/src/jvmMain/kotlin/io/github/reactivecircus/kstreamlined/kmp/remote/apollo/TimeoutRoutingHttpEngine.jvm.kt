package io.github.reactivecircus.kstreamlined.kmp.remote.apollo

import com.apollographql.apollo.network.http.DefaultHttpEngine
import com.apollographql.apollo.network.http.HttpEngine
import okhttp3.OkHttpClient
import kotlin.time.toJavaDuration

/**
 * Creates an [HttpEngine] backed by [okHttpClient].
 *
 * Operations with a [requestTimeout] are executed with a client derived from [okHttpClient]
 * with call and read timeouts of the request timeout.
 * Derived clients share the connection pool and dispatcher with [okHttpClient].
 */
public fun createTimeoutRoutingHttpEngine(okHttpClient: () -> OkHttpClient): HttpEngine =
    TimeoutRoutingHttpEngine(
        defaultEngine = DefaultHttpEngine(okHttpClient),
        timeoutEngineFactory = { timeout ->
            DefaultHttpEngine {
                okHttpClient().newBuilder()
                    .callTimeout(timeout.toJavaDuration())
                    .readTimeout(timeout.toJavaDuration())
                    .build()
            }
        },
    )
