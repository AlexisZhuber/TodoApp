package com.alexismoraportal.todoapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalDate
import java.time.LocalTime
import com.alexismoraportal.todoapp.ui.theme.Primary
import com.alexismoraportal.todoapp.ui.theme.Secondary

/**
 * DateTimePickers.kt
 *
 * This file has two composables for displaying separate date and time pickers using MaterialDialogs.
 * They respect the Dependency Inversion Principle by taking states and callbacks as parameters
 * rather than depending on concrete implementations from the outside.
 */

@Composable
fun DatePickerDialog(
    dateDialogState: com.vanpra.composematerialdialogs.MaterialDialogState,
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var tempDate = initialDate

    MaterialDialog(
        dialogState = dateDialogState,
        onCloseRequest = { dateDialogState.hide() },
        buttons = {
            positiveButton(
                text = "OK",
                textStyle = TextStyle(color = Color.Black)
            ) {
                onDateSelected(tempDate)
            }
            negativeButton(
                text = "Cancel",
                textStyle = TextStyle(color = Color.Black)
            )
        }
    ) {
        datepicker(
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = Secondary,
                headerTextColor = Color.Black,
                calendarHeaderTextColor = Color.Black,
                dateActiveBackgroundColor = Secondary,
                dateInactiveBackgroundColor = Color.White,
                dateActiveTextColor = Color.White,
                dateInactiveTextColor = Color.Black
            ),
            initialDate = initialDate,
            title = "Select Date",
            allowedDateValidator = { it >= LocalDate.now() }
        ) { date ->
            tempDate = date
        }
    }
}

@Composable
fun TimePickerDialog(
    timeDialogState: com.vanpra.composematerialdialogs.MaterialDialogState,
    onTimeSelected: (LocalTime) -> Unit
) {
    var tempTime = LocalTime.now()

    MaterialDialog(
        dialogState = timeDialogState,
        onCloseRequest = { timeDialogState.hide() },
        buttons = {
            positiveButton("OK", textStyle = TextStyle(color = Color.Black)) {
                onTimeSelected(tempTime)
            }
            negativeButton("Cancel", textStyle = TextStyle(color = Color.Black))
        }
    ) {
        timepicker(
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = Secondary,
                inactiveBackgroundColor = Color.White,
                activeTextColor = Color.Black,
                inactiveTextColor = Color.Black,
                inactivePeriodBackground = Color.White,
                selectorColor = Primary,
                selectorTextColor = Color.Black,
                headerTextColor = Color.Black,
                borderColor = Primary
            ),
            initialTime = LocalTime.now(),
            title = "Select Time",
            is24HourClock = true
        ) { time ->
            tempTime = time
        }
    }
}
