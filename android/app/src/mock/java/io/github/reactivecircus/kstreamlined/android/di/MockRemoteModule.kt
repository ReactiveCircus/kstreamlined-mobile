package io.github.reactivecircus.kstreamlined.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.remote.FeedService
import io.github.reactivecircus.kstreamlined.kmp.remote.MockFeedService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockRemoteModule {

    @Provides
    @Singleton
    fun feedService(): FeedService {
        return MockFeedService()
    }
}
