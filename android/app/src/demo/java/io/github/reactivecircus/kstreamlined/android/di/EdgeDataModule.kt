package io.github.reactivecircus.kstreamlined.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmm.data.feed.EdgeFeedRepo
import io.github.reactivecircus.kstreamlined.kmm.data.feed.FeedRepo
import io.github.reactivecircus.kstreamlined.kmm.data.util.ApiErrorChecker
import io.github.reactivecircus.kstreamlined.kmm.data.util.KtorApiErrorChecker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EdgeDataModule {

    @Provides
    @Singleton
    fun feedRepo(): FeedRepo {
        return EdgeFeedRepo()
    }

    @Provides
    @Singleton
    fun apiErrorChecker(): ApiErrorChecker {
        return KtorApiErrorChecker
    }
}
