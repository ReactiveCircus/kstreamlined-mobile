package io.github.reactivecircus.kstreamlined.kmp.database.testing

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase

internal actual fun createInMemoryDriver(): SqlDriver {
    return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        KStreamlinedDatabase.Schema.create(this)
    }
}
