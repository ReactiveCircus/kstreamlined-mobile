package io.github.reactivecircus.kstreamlined.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.networking.FakeFeedService
import io.github.reactivecircus.kstreamlined.kmp.networking.FeedService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockDataModule {

    @Provides
    @Singleton
    fun feedService(): FeedService {
        return FakeFeedService()
    }
}
