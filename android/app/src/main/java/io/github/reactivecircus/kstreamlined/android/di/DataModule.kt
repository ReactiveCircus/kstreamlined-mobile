package io.github.reactivecircus.kstreamlined.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngineImpl
import io.github.reactivecircus.kstreamlined.kmp.networking.FeedService
import kotlinx.coroutines.CoroutineScope
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
    fun feedSyncEngine(
        feedService: FeedService,
        database: KStreamlinedDatabase,
        @AppCoroutineScope scope: CoroutineScope,
    ): FeedSyncEngine {
        return FeedSyncEngineImpl(
            feedService = feedService,
            db = database,
            syncEngineScope = scope,
        )
    }
}
