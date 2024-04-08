package io.github.reactivecircus.kstreamlined.android.di

import androidx.tracing.trace
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.store
import com.apollographql.apollo3.network.http.ApolloClientAwarenessInterceptor
import com.apollographql.apollo3.network.okHttpCallFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.android.BuildConfig
import io.github.reactivecircus.kstreamlined.kmp.remote.apollo.ApolloClientConfigs
import okhttp3.Call
import okhttp3.OkHttpClient
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = trace("KSOkHttpClient") {
        val callTimeout = BuildConfig.NETWORK_TIMEOUT_SECONDS
            .toDuration(DurationUnit.SECONDS)
            .toJavaDuration()
        OkHttpClient.Builder()
            .callTimeout(callTimeout)
            .build()
    }

    @Provides
    @Singleton
    fun apolloClient(
        okHttpCallFactory: Lazy<Call.Factory>,
    ): ApolloClient = trace("ApolloClient") {
        return ApolloClient.Builder()
            .okHttpCallFactory(okHttpCallFactory.get())
            .serverUrl(BuildConfig.API_ENDPOINT)
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
