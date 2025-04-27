package io.github.reactivecircus.kstreamlined.android.di

import android.content.Context
import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.InstantAdapter
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.LastSyncMetadata
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun database(
        @ApplicationContext context: Context
    ): KStreamlinedDatabase {
        return KStreamlinedDatabase(
            AndroidSqliteDriver(
                schema = KStreamlinedDatabase.Schema,
                context = context,
                name = "kstreamlined.db",
            ),
            feedItemEntityAdapter = FeedItemEntity.Adapter(
                publish_timeAdapter = InstantAdapter,
                podcast_description_formatAdapter = EnumColumnAdapter(),
            ),
            lastSyncMetadataAdapter = LastSyncMetadata.Adapter(
                resource_typeAdapter = EnumColumnAdapter(),
                last_sync_timeAdapter = InstantAdapter,
            )
        )
    }
}
