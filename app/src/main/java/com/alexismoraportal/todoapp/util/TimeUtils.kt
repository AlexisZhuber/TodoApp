package com.alexismoraportal.todoapp.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * TimeUtils.kt
 *
 * A utility file for date/time parsing and formatting logic
 * that doesn't depend on Compose, making it reusable across the project.
 */

/**
 * Parses a date/time string "dd/MM/yyyy HH:mm" into a LocalDateTime.
 */
fun parseDateTime(dateTimeString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return LocalDateTime.parse(dateTimeString, formatter)
}

/**
 * Converts a Duration into a user-friendly string like:
 *  - "Overdue by 1d 2h 3m 10s" if negative
 *  - "Time left: 1d 2h 3m 10s" otherwise
 */
fun formatDuration(duration: Duration): String {
    // If duration is negative, the scheduled date/time is in the past
    return if (duration.isNegative) {
        val past = duration.abs()
        "Overdue by ${daysHoursMinsSecs(past)}"
    } else {
        "Time left: ${daysHoursMinsSecs(duration)}"
    }
}

/**
 * Helper that breaks a Duration into days, hours, minutes, seconds
 * and forms a short string "Xd Xh Xm Xs".
 */
fun daysHoursMinsSecs(d: Duration): String {
    val days = d.toDays()
    val hours = d.toHours() % 24
    val minutes = d.toMinutes() % 60
    val seconds = d.seconds % 60

    val sb = StringBuilder()
    if (days > 0) sb.append("${days}d ")
    if (hours > 0) sb.append("${hours}h ")
    if (minutes > 0) sb.append("${minutes}m ")
    sb.append("${seconds}s")

    return sb.toString().trim()
}
