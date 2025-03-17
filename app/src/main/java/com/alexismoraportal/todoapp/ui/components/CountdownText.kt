package com.alexismoraportal.todoapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import com.alexismoraportal.todoapp.util.parseDateTime
import com.alexismoraportal.todoapp.util.daysHoursMinsSecs
import com.alexismoraportal.todoapp.ui.theme.Primary

/**
 * CountdownText.kt
 *
 * A reusable composable that displays either:
 * - "Time left: ..." if there is still time, or
 * - "Task Overdue" if the scheduled date/time is in the past.
 *
 * It updates every second using a LaunchedEffect.
 */
@Composable
fun CountdownText(
    scheduledDateTimeString: String,
    style: TextStyle,
) {
    // Convert the "scheduledDateTimeString" to a LocalDateTime once
    val scheduledDateTime = remember(scheduledDateTimeString) {
        parseDateTime(scheduledDateTimeString)
    }

    // State for current system time, updated every second
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }

    // Update current time every second
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalDateTime.now()
            delay(1000)
        }
    }

    // Calculate how much time is left or overdue
    val duration = remember(currentTime, scheduledDateTime) {
        Duration.between(currentTime, scheduledDateTime)
    }

    // Si duration es negativa, mostramos "Task Overdue", de lo contrario "Time left: …"
    val displayText = remember(duration) {
        if (duration.isNegative) {
            "Task Overdue"
        } else {
            "Time left: ${daysHoursMinsSecs(duration)}"
        }
    }

    // Cambiamos el color si está vencida o si todavía hay tiempo
    val textColor = if (duration.isNegative) Color.LightGray else Primary

    // Render the countdown or overdue text
    Text(
        text = displayText,
        style = style,
        color = textColor
    )
}


