package io.github.reactivecircus.kstreamlined.kmp.database.testing

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.driver.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase

internal actual fun createInMemoryDriver(): SqlDriver {
    index++
    val schema = KStreamlinedDatabase.Schema
    return NativeSqliteDriver(
        DatabaseConfiguration(
            name = "kstreamlined-test-$index.db",
            version = schema.version.toInt(),
            create = { connection ->
                wrapConnection(connection) { schema.create(it) }
            },
            upgrade = { connection, oldVersion, newVersion ->
                wrapConnection(connection) {
                    schema.migrate(it, oldVersion.toLong(), newVersion.toLong())
                }
            },
            inMemory = true,
        ),
    )
}

private var index = 0
