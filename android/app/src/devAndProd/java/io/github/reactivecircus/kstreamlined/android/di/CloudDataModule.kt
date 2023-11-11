package io.github.reactivecircus.kstreamlined.android.di

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.data.feed.CloudFeedRepo
import io.github.reactivecircus.kstreamlined.kmp.data.feed.FeedRepo
import io.github.reactivecircus.kstreamlined.kmp.data.util.ApiErrorChecker
import io.github.reactivecircus.kstreamlined.kmp.data.util.ApolloApiErrorChecker
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
