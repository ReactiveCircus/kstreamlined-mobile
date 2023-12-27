package io.github.reactivecircus.kstreamlined.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FakeFeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockDataModule {

    @Provides
    @Singleton
    fun feedDataSource(): FeedDataSource {
        return FakeFeedDataSource()
    }
}
