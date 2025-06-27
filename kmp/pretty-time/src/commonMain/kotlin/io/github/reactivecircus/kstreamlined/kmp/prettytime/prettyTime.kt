package io.github.reactivecircus.kstreamlined.kmp.prettytime

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

@Suppress("MagicNumber")
public fun Instant.weeksAgo(
    clock: Clock = Clock.System
): String {
    val duration = clock.now().minus(this)
    val weekDifference = duration.inWholeDays / 7
    return when (weekDifference) {
        0L -> "This week"
        1L -> "Last week"
        else -> "Earlier"
    }
}

@Suppress("MagicNumber")
public fun Instant.timeAgo(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): String {
    val now = clock.now()
    val duration = now.minus(this)
    return when {
        duration.inWholeMinutes < 1 -> "Moments ago"
        duration.inWholeHours < 1 -> if (duration.inWholeMinutes == 1L) {
            "${duration.inWholeMinutes} minute ago"
        } else {
            "${duration.inWholeMinutes} minutes ago"
        }
        duration.inWholeDays < 1 -> if (duration.inWholeHours == 1L) {
            "${duration.inWholeHours} hour ago"
        } else {
            "${duration.inWholeHours} hours ago"
        }
        duration.inWholeDays < 2 -> "Yesterday"
        duration.inWholeDays < 7 -> "${duration.inWholeDays} days ago"
        else -> toFormattedTime(timeZone)
    }
}

/**
 * Format the [Instant] to a string in the format of `dd MMM yyyy`.
 */
@Suppress("MagicNumber")
public fun Instant.toFormattedTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    return toLocalDateTime(timeZone).let { localDateTime ->
        buildString {
            append(
                localDateTime.day.toString().padStart(2, '0')
            )
            append(" ")
            append(
                localDateTime.month.name.take(3)
                    .lowercase().replaceFirstChar { it.titlecase() }
            )
            append(" ")
            append(localDateTime.year)
        }
    }
}
