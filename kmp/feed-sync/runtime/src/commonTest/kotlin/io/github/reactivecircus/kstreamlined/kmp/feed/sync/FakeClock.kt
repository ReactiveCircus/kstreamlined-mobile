package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import kotlin.time.Clock
import kotlin.time.Instant

class FakeClock : Clock {
    var currentTime: Instant = Instant.parse("2023-12-03T03:10:54Z")
    override fun now(): Instant {
        return currentTime
    }
}
