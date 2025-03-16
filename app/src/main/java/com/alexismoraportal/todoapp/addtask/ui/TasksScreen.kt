package com.alexismoraportal.todoapp.addtask.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Deck
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexismoraportal.todoapp.addtask.data.model.Task
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.alexismoraportal.todoapp.ui.theme.Primary
import com.alexismoraportal.todoapp.ui.theme.Secondary
import com.alexismoraportal.todoapp.ui.theme.Tertiary
import com.alexismoraportal.todoapp.ui.theme.Quaternary
import com.alexismoraportal.todoapp.ui.theme.Quinary
import com.alexismoraportal.todoapp.ui.theme.Senary
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults

@Composable
fun TasksScreen(taskViewModel: TasksViewModel) {
    // List of tasks
    val tasks = remember { mutableStateListOf<Task>() }

    // States for controlling the main "New Note" AlertDialog
    var showDialog by remember { mutableStateOf(false) }
    var taskText by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf<ImageVector?>(null) }

    // State for the combined selected date and time (scheduled time)
    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    // Temporary state to store the selected date from the date dialog
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    // MaterialDialog states for date and time pickers
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    val context = LocalContext.current

    // Icon options list
    val iconOptions = listOf(
        Icons.Filled.Android,
        Icons.Filled.Warning,
        Icons.Filled.Star,
        Icons.Filled.Adjust,
        Icons.Filled.AirplanemodeActive,
        Icons.Filled.Deck,
        Icons.Filled.Report,
        Icons.Filled.AccessTime,
        Icons.Filled.AddLocation,
        Icons.Filled.AddLink,
        Icons.Filled.Computer,
        Icons.Filled.DirectionsCar,
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Tertiary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Primary)
        ) {
            // Centered title (text remains black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NOTES APP",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // List of tasks displayed in a LazyColumn; each task is shown in a Card with a Secondary background
            LazyColumn {
                items(tasks) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Secondary),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = task.icon,
                                    contentDescription = "icon",
                                    modifier = Modifier.size(24.dp),
                                    tint = Secondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = task.text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            // Display creation and scheduled dates
                            Text(
                                text = "Created: ${task.createdDateTime}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black
                            )
                            Text(
                                text = "Scheduled: ${task.scheduledDateTime}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // AlertDialog for adding a new note; containerColor set to Senary
            if (showDialog) {
                AlertDialog(
                    containerColor = Senary,
                    onDismissRequest = {
                        showDialog = false
                        // Reset values when dismissing without saving
                        taskText = ""
                        selectedIcon = null
                        selectedDateTime = null
                    },
                    title = {
                        Text(
                            text = "ADD NEW NOTE",
                            color = Color.Black,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    text = {
                        Column {
                            // Text field for task description with Tertiary background and Secondary focused indicator/label
                            OutlinedTextField(
                                value = taskText,
                                onValueChange = { taskText = it },
                                label = { Text(text = "Task") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    focusedIndicatorColor = Secondary,
                                    unfocusedContainerColor = Tertiary,
                                    focusedContainerColor = Tertiary,
                                    focusedLabelColor = Secondary
                                )
                            )
                            Spacer(Modifier.height(16.dp))
                            // Button to select date and time (opens date dialog first)
                            Button(
                                onClick = { dateDialogState.show() },
                                shape = RoundedCornerShape(32.dp),
                                border = BorderStroke(1.dp, Quaternary),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Select date and time",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            // Display the selected date/time if available
                            selectedDateTime?.let { dateTime ->
                                Text(
                                    text = "Selected: ${dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            // Label for icon selection
                            Text(
                                text = "Select an icon",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            // Horizontal scroll row for selecting an icon
                            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                iconOptions.forEach { icon ->
                                    IconButton(onClick = { selectedIcon = icon }) {
                                        Icon(
                                            modifier = Modifier.size(32.dp),
                                            imageVector = icon,
                                            contentDescription = "icon",
                                            tint = if (selectedIcon == icon) Tertiary else Secondary
                                        )
                                    }
                                }
                            }
                        }
                    },
                    // "Save" button enabled only if task text, an icon, and a valid date/time are provided
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if (taskText.isNotBlank() && selectedIcon != null && selectedDateTime != null) {
                                    if (selectedDateTime!!.isBefore(LocalDateTime.now())) {
                                        Toast.makeText(
                                            context,
                                            "Selected date and time cannot be in the past",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@TextButton
                                    }
                                    // Add the task with current creation time and the user-selected scheduled time
                                    tasks.add(
                                        Task(
                                            text = taskText,
                                            icon = selectedIcon!!,
                                            createdDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                                            scheduledDateTime = selectedDateTime!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                                        )
                                    )
                                }
                                // Reset state and close dialog
                                taskText = ""
                                selectedIcon = null
                                selectedDateTime = null
                                showDialog = false
                            },
                            enabled = taskText.isNotBlank() && selectedIcon != null && selectedDateTime != null
                        ) {
                            Text(
                                text = "Save",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                taskText = ""
                                selectedIcon = null
                                selectedDateTime = null
                                showDialog = false
                            }
                        ) {
                            Text(
                                text = "Cancel",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                )
            }

            // Temporary variable for date selection inside the date dialog
            var tempDate by remember { mutableStateOf(LocalDate.now()) }
            // Date dialog for selecting the date with buttons
            MaterialDialog(
                dialogState = dateDialogState,
                onCloseRequest = { dateDialogState.hide() },
                buttons = {
                    positiveButton(
                        text = "OK",
                        textStyle = TextStyle(color = Color.Black)
                    ) {
                        // When "OK" is pressed, store the selected date,
                        // hide the date dialog, and then show the time dialog.
                        selectedDate = tempDate
                        dateDialogState.hide()
                        timeDialogState.show()
                    }
                    negativeButton(
                        text = "Cancel",
                        textStyle = TextStyle(color = Color.Black)
                    )
                }
            ) {
                datepicker(
                    colors = DatePickerDefaults.colors(
                        headerBackgroundColor = Primary,
                        headerTextColor = Color.Black,
                        calendarHeaderTextColor = Color.Black,
                        dateActiveBackgroundColor = Secondary,
                        dateInactiveBackgroundColor = Tertiary,
                        dateActiveTextColor = Color.Black,
                        dateInactiveTextColor = Color.Black
                    ),
                    initialDate = LocalDate.now(),
                    title = "Select Date",
                    allowedDateValidator = { it >= LocalDate.now() }
                ) { date ->
                    tempDate = date
                }
            }

            // Temporary variable for time selection inside the time dialog
            var tempTime by remember { mutableStateOf(LocalTime.now()) }
            // Time dialog for selecting the time with buttons
            MaterialDialog(
                dialogState = timeDialogState,
                onCloseRequest = { timeDialogState.hide() },
                buttons = {
                    positiveButton("OK", textStyle = TextStyle(color = Color.Black)) {
                        // Combine the selected date with the chosen time
                        if (selectedDate != null) {
                            val dateTime = LocalDateTime.of(selectedDate, tempTime)
                            selectedDateTime = if (dateTime >= LocalDateTime.now()) dateTime else null
                        }
                        timeDialogState.hide()
                    }
                    negativeButton("Cancel", textStyle = TextStyle(color = Color.Black))
                }
            ) {
                timepicker(
                    colors = TimePickerDefaults.colors(
                        activeBackgroundColor = Primary,
                        inactiveBackgroundColor = Secondary,
                        activeTextColor = Color.Black,
                        inactiveTextColor = Color.Black,
                        inactivePeriodBackground = Tertiary,
                        selectorColor = Quaternary,
                        selectorTextColor = Color.Black,
                        headerTextColor = Color.Black,
                        borderColor = Quaternary
                    ),
                    initialTime = LocalTime.now(),
                    title = "Select Time",
                    is24HourClock = true
                ) { time ->
                    tempTime = time
                }
            }
        }
    }
}
