package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

internal data class SyncConfig(
    val feedSourcesCacheMaxAge: Duration,
    val feedItemsCacheMaxAge: Duration,
) {
    companion object {
        val Default = SyncConfig(
            feedSourcesCacheMaxAge = 1.days,
            feedItemsCacheMaxAge = 1.hours,
        )
    }
}
