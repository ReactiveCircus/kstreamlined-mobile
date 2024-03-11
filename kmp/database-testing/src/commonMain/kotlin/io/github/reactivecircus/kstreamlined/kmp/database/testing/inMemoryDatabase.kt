package io.github.reactivecircus.kstreamlined.kmp.database.testing

import app.cash.sqldelight.db.SqlDriver
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.InstantAdapter
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase

public fun createInMemoryDatabase(): KStreamlinedDatabase {
    return KStreamlinedDatabase(createInMemoryDriver(), FeedItemEntity.Adapter(InstantAdapter))
}

internal expect fun createInMemoryDriver(): SqlDriver
