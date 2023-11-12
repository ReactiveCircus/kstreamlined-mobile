package io.github.reactivecircus.kstreamlined.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.datasource.FakeFeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.datasource.util.ApiErrorChecker
import io.github.reactivecircus.kstreamlined.kmp.datasource.util.NoOpApiErrorChecker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockDataModule {

    @Provides
    @Singleton
    fun feedDataSource(): FeedDataSource {
        return FakeFeedDataSource()
    }

    @Provides
    @Singleton
    fun apiErrorChecker(): ApiErrorChecker {
        return NoOpApiErrorChecker
    }
}
