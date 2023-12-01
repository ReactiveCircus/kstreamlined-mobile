package io.github.reactivecircus.kstreamlined.kmp.persistence.database

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

public val InstantAdapter: ColumnAdapter<Instant, String> = object : ColumnAdapter<Instant, String> {
    override fun decode(databaseValue: String): Instant {
        return databaseValue.toInstant()
    }

    override fun encode(value: Instant): String {
        return value.toString()
    }
}
