package io.github.reactivecircus.kstreamlined.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncer
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncerImpl
import io.github.reactivecircus.kstreamlined.kmp.networking.FeedService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun feedDataSource(
        feedService: FeedService,
        database: KStreamlinedDatabase,
    ): FeedDataSource {
        return FeedDataSource(feedService, database)
    }

    @Provides
    @Singleton
    fun feedSyncer(
        feedService: FeedService,
        database: KStreamlinedDatabase,
    ): FeedSyncer {
        return FeedSyncerImpl(feedService, database)
    }
}
