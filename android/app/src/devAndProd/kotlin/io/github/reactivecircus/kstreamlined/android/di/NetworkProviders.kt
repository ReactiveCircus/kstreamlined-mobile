package io.github.reactivecircus.kstreamlined.android.di

import androidx.tracing.trace
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.http.HttpMethod
import com.apollographql.apollo.cache.normalized.store
import com.apollographql.apollo.network.http.ApolloClientAwarenessInterceptor
import com.apollographql.apollo.network.okHttpCallFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.reactivecircus.kstreamlined.android.BuildConfig
import io.github.reactivecircus.kstreamlined.kmp.remote.apollo.ApolloClientConfigs
import okhttp3.Call

interface NetworkProviders {
    @SingleIn(AppScope::class)
    @Provides
    fun apolloClient(
        okHttpCallFactory: Lazy<Call.Factory>,
    ): ApolloClient = trace("ApolloClient") {
        return ApolloClient.Builder()
            .okHttpCallFactory { okHttpCallFactory.value }
            .serverUrl(BuildConfig.API_ENDPOINT)
            .autoPersistedQueries(httpMethodForHashedQueries = HttpMethod.Post)
            .addHttpInterceptor(
                ApolloClientAwarenessInterceptor(
                    clientName = "KStreamlined Android",
                    clientVersion = BuildConfig.VERSION_NAME,
                ),
            )
            .store(ApolloClientConfigs.apolloStore, writeToCacheAsynchronously = true)
            .build()
    }
}
