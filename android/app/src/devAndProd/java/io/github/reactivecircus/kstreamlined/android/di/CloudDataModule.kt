package io.github.reactivecircus.kstreamlined.android.di

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmm.data.feed.CloudFeedRepo
import io.github.reactivecircus.kstreamlined.kmm.data.feed.FeedRepo
import io.github.reactivecircus.kstreamlined.kmm.data.util.ApiErrorChecker
import io.github.reactivecircus.kstreamlined.kmm.data.util.ApolloApiErrorChecker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudDataModule {

    @Provides
    @Singleton
    fun feedRepo(apolloClient: ApolloClient): FeedRepo {
        return CloudFeedRepo(apolloClient)
    }

    @Provides
    @Singleton
    fun apiErrorChecker(): ApiErrorChecker {
        return ApolloApiErrorChecker
    }
}
