package io.github.reactivecircus.kstreamlined.android.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.kstreamlined.kmp.persistence.database.KStreamlinedDatabase
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
            )
        )
    }
}
