package io.github.reactivecircus.kstreamlined.kmp.database

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.Instant

public val InstantAdapter: ColumnAdapter<Instant, String> = object : ColumnAdapter<Instant, String> {
    override fun decode(databaseValue: String): Instant = Instant.parse(databaseValue)

    override fun encode(value: Instant): String = value.toString()
}
