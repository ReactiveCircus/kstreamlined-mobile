package io.github.reactivecircus.kstreamlined.kmp.remote.apollo

import com.apollographql.apollo.network.http.HttpEngine
import okhttp3.OkHttpClient
import kotlin.time.Duration
import kotlin.time.toJavaDuration

actual fun platformTimeoutRoutingHttpEngine(defaultTimeout: Duration): HttpEngine {
    val okHttpClient = OkHttpClient.Builder()
        .callTimeout(defaultTimeout.toJavaDuration())
        .readTimeout(defaultTimeout.toJavaDuration())
        .build()
    return createTimeoutRoutingHttpEngine { okHttpClient }
}
