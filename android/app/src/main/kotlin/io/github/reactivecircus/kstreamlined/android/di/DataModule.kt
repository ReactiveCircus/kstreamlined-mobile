package io.github.reactivecircus.kstreamlined.android.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngineImpl
import io.github.reactivecircus.kstreamlined.kmp.networkmonitor.AndroidNetworkMonitor
import io.github.reactivecircus.kstreamlined.kmp.networkmonitor.NetworkMonitor
import io.github.reactivecircus.kstreamlined.kmp.remote.FeedService
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.SettingsDataSource
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.datastore.createAppSettingsDataStore
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
        networkMonitor: NetworkMonitor,
        @AppCoroutineScope scope: CoroutineScope,
    ): FeedSyncEngine {
        return FeedSyncEngineImpl(
            feedService = feedService,
            db = database,
            networkMonitor = networkMonitor,
            syncEngineScope = scope,
        )
    }

    @Provides
    @Singleton
    fun settingsDataSource(
        @ApplicationContext context: Context,
    ): SettingsDataSource {
        return SettingsDataSource(dataStore = createAppSettingsDataStore(context))
    }

    @Provides
    @Singleton
    fun networkMonitor(
        @ApplicationContext context: Context,
        @AppCoroutineScope scope: CoroutineScope,
    ): NetworkMonitor {
        return AndroidNetworkMonitor(context, scope)
    }
}
