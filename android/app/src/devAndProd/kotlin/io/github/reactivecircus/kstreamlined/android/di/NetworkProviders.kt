package io.github.reactivecircus.kstreamlined.android.di

import androidx.tracing.trace
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.http.HttpMethod
import com.apollographql.apollo.network.http.ApolloClientAwarenessInterceptor
import com.apollographql.cache.normalized.cacheManager
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.reactivecircus.kstreamlined.android.BuildConfig
import io.github.reactivecircus.kstreamlined.kmp.remote.apollo.ApolloClientConfigs
import io.github.reactivecircus.kstreamlined.kmp.remote.apollo.createTimeoutRoutingHttpEngine
import okhttp3.OkHttpClient

interface NetworkProviders {
    @SingleIn(AppScope::class)
    @Provides
    private fun apolloClient(
        okHttpClient: Lazy<OkHttpClient>,
    ): ApolloClient = trace("ApolloClient") {
        return ApolloClient.Builder()
            .httpEngine(createTimeoutRoutingHttpEngine { okHttpClient.value })
            .serverUrl(BuildConfig.API_ENDPOINT)
            .autoPersistedQueries(httpMethodForHashedQueries = HttpMethod.Post)
            .addHttpInterceptor(
                ApolloClientAwarenessInterceptor(
                    clientName = "KStreamlined Android",
                    clientVersion = BuildConfig.VERSION_NAME,
                ),
            )
            .cacheManager(ApolloClientConfigs.cacheManager, writeToCacheAsynchronously = true)
            .build()
    }
}
