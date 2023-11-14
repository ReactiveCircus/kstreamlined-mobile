package io.github.reactivecircus.kstreamlined.android.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.store
import com.apollographql.apollo3.network.http.ApolloClientAwarenessInterceptor
import com.apollographql.apollo3.network.http.DefaultHttpEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.android.BuildConfig
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.networking.ApolloClientConfigs
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Provides
    @Singleton
    fun apolloClient(): ApolloClient {
        val timeoutMillis = BuildConfig.NETWORK_TIMEOUT_SECONDS
            .toDuration(DurationUnit.SECONDS)
            .inWholeMilliseconds

        return ApolloClient.Builder()
            .serverUrl(BuildConfig.API_ENDPOINT)
            .httpEngine(
                DefaultHttpEngine(
                    timeoutMillis = timeoutMillis
                )
            )
            .addHttpInterceptor(
                ApolloClientAwarenessInterceptor(
                    clientName = "KStreamlined Android",
                    clientVersion = BuildConfig.VERSION_NAME,
                )
            )
            .store(ApolloClientConfigs.apolloStore, writeToCacheAsynchronously = true)
            .build()
    }
}
