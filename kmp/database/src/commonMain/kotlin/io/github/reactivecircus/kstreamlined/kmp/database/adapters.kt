package io.github.reactivecircus.kstreamlined.kmp.database

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant

public val InstantAdapter: ColumnAdapter<Instant, String> = object : ColumnAdapter<Instant, String> {
    override fun decode(databaseValue: String): Instant {
        return Instant.parse(databaseValue)
    }

    override fun encode(value: Instant): String {
        return value.toString()
    }
}
