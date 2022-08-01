package io.github.reactivecircus.kstreamlined.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmm.data.feed.FakeFeedRepo
import io.github.reactivecircus.kstreamlined.kmm.data.feed.FeedRepo
import io.github.reactivecircus.kstreamlined.kmm.data.util.ApiErrorChecker
import io.github.reactivecircus.kstreamlined.kmm.data.util.NoOpApiErrorChecker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockDataModule {

    @Provides
    @Singleton
    fun feedRepo(): FeedRepo {
        return FakeFeedRepo()
    }

    @Provides
    @Singleton
    fun apiErrorChecker(): ApiErrorChecker {
        return NoOpApiErrorChecker
    }
}
