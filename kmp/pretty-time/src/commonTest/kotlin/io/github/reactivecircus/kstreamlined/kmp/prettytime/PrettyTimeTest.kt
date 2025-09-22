package io.github.reactivecircus.kstreamlined.kmp.prettytime

import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class PrettyTimeTest {
    private val fixedClock = object : Clock {
        override fun now(): Instant = Instant.parse("2023-12-03T03:10:54Z")
    }

    @Test
    fun weeksAgo() {
        val now = fixedClock.now()
        assertEquals("This week", now.minus(1.seconds).weeksAgo(fixedClock))
        assertEquals("This week", now.minus(7.days - 1.seconds).weeksAgo(fixedClock))
        assertEquals("Last week", now.minus(7.days).weeksAgo(fixedClock))
        assertEquals("Last week", now.minus(14.days - 1.seconds).weeksAgo(fixedClock))
        assertEquals("Earlier", now.minus(14.days).weeksAgo(fixedClock))
        assertEquals("Earlier", now.minus(100.days).weeksAgo(fixedClock))
    }

    @Test
    fun timeAgp() {
        val fixedClock = object : Clock {
            override fun now(): Instant = Instant.parse("2023-12-03T03:10:54Z")
        }
        val now = fixedClock.now()
        val timeZone = TimeZone.UTC
        assertEquals("Moments ago", now.minus(1.seconds).timeAgo(fixedClock, timeZone))
        assertEquals("Moments ago", now.minus(59.seconds).timeAgo(fixedClock, timeZone))
        assertEquals("1 minute ago", now.minus(60.seconds).timeAgo(fixedClock, timeZone))
        assertEquals("59 minutes ago", now.minus(59.minutes).timeAgo(fixedClock, timeZone))
        assertEquals("1 hour ago", now.minus(60.minutes).timeAgo(fixedClock, timeZone))
        assertEquals("23 hours ago", now.minus(23.hours).timeAgo(fixedClock, timeZone))
        assertEquals("Yesterday", now.minus(24.hours).timeAgo(fixedClock, timeZone))
        assertEquals("Yesterday", now.minus(47.hours).timeAgo(fixedClock, timeZone))
        assertEquals("2 days ago", now.minus(48.hours).timeAgo(fixedClock, timeZone))
        assertEquals("6 days ago", now.minus(7.days - 1.seconds).timeAgo(fixedClock, timeZone))
        assertEquals("26 Nov 2023", now.minus(7.days).timeAgo(fixedClock, timeZone))
        assertEquals("03 Dec 2022", now.minus(365.days).timeAgo(fixedClock, timeZone))
    }

    @Test
    fun toFormattedTime() {
        val timeZone = TimeZone.UTC
        assertEquals("03 Dec 2023", Instant.parse("2023-12-03T03:10:54Z").toFormattedTime(timeZone))
        assertEquals("23 Oct 2023", Instant.parse("2023-10-23T12:00:54Z").toFormattedTime(timeZone))
        assertEquals("21 Nov 2022", Instant.parse("2022-11-21T23:00:00Z").toFormattedTime(timeZone))
    }
}
