package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

class FakeClock : Clock {
    var currentTime: Instant = "2023-12-03T03:10:54Z".toInstant()
    override fun now(): Instant {
        return currentTime
    }
}
