package io.github.reactivecircus.kstreamlined.kmp.database.testing

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.InstantAdapter
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.LastSyncMetadata

public fun createInMemoryDatabase(): KStreamlinedDatabase {
    return KStreamlinedDatabase(
        createInMemoryDriver(),
        FeedItemEntity.Adapter(InstantAdapter),
        LastSyncMetadata.Adapter(
            resource_typeAdapter = EnumColumnAdapter(),
            last_sync_timeAdapter = InstantAdapter,
        ),
    )
}

internal expect fun createInMemoryDriver(): SqlDriver
