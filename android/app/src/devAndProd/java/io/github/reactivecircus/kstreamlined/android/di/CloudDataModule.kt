package io.github.reactivecircus.kstreamlined.android.di

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.CloudFeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudDataModule {

    @Provides
    @Singleton
    fun feedDataSource(apolloClient: ApolloClient): FeedDataSource {
        return CloudFeedDataSource(apolloClient)
    }
}
