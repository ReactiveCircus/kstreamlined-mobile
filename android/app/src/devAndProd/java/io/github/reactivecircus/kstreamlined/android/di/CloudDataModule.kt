package io.github.reactivecircus.kstreamlined.android.di

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.remote.CloudFeedService
import io.github.reactivecircus.kstreamlined.kmp.remote.FeedService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudDataModule {

    @Provides
    @Singleton
    fun feedService(apolloClient: ApolloClient): FeedService {
        return CloudFeedService(apolloClient)
    }
}
