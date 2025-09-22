package io.github.reactivecircus.kstreamlined.android.di

import androidx.tracing.trace
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.store
import com.apollographql.apollo.network.http.ApolloClientAwarenessInterceptor
import com.apollographql.apollo.network.okHttpCallFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.android.BuildConfig
import io.github.reactivecircus.kstreamlined.kmp.remote.CloudFeedService
import io.github.reactivecircus.kstreamlined.kmp.remote.FeedService
import io.github.reactivecircus.kstreamlined.kmp.remote.apollo.ApolloClientConfigs
import okhttp3.Call
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudRemoteModule {
    @Provides
    @Singleton
    fun feedService(apolloClient: ApolloClient): FeedService {
        return CloudFeedService(apolloClient)
    }

    @Provides
    @Singleton
    fun apolloClient(
        okHttpCallFactory: Lazy<Call.Factory>,
    ): ApolloClient = trace("ApolloClient") {
        return ApolloClient.Builder()
            .okHttpCallFactory { okHttpCallFactory.get() }
            .serverUrl(BuildConfig.API_ENDPOINT)
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
