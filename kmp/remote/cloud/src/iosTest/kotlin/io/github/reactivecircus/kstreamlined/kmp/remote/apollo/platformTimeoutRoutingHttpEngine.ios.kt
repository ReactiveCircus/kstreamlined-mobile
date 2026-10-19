package io.github.reactivecircus.kstreamlined.kmp.remote.apollo

import com.apollographql.apollo.network.http.HttpEngine
import kotlin.time.Duration

actual fun platformTimeoutRoutingHttpEngine(defaultTimeout: Duration): HttpEngine =
    createTimeoutRoutingHttpEngine(defaultTimeout)
