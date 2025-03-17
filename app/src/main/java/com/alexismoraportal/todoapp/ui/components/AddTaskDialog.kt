package com.alexismoraportal.todoapp.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexismoraportal.todoapp.domain.TaskEntity
import com.alexismoraportal.todoapp.ui.theme.Primary
import com.alexismoraportal.todoapp.ui.theme.Secondary
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * AddTaskDialog
 *
 * A composable dialog for creating a new Task. This dialog includes separate fields for:
 * - A unique task name (alphanumeric and no more than 20 characters)
 * - A task description
 * - Date and time pickers for scheduling the task
 * - Icon selection from a list of provided options.
 *
 * The "Save" button is enabled only when all fields are complete.
 *
 * Note: The new TaskEntity uses iconIndex (an Int) to represent the chosen icon.
 */
@Composable
fun AddTaskDialog(
    showDialog: Boolean,
    iconOptions: List<ImageVector>,
    onConfirm: (TaskEntity) -> Unit,
    onDismiss: () -> Unit
) {
    if (!showDialog) return

    val context = LocalContext.current

    // Local states for new task fields: name, description, selected icon, and scheduled date/time.
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf<ImageVector?>(null) }
    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    // States for Date & Time pickers.
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "ADD NEW TASK",
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                // Outlined text field for task name.
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { if (it.length <= 20) taskName = it },
                    label = { Text(text = "Task Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Primary,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedLabelColor = Primary,
                        unfocusedIndicatorColor = Secondary,
                        unfocusedLabelColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                // Outlined text field for task description.
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text(text = "Task Description") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Primary,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedLabelColor = Primary,
                        unfocusedIndicatorColor = Secondary,
                        unfocusedLabelColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Button to open the DatePickerDialog.
                Button(
                    onClick = { dateDialogState.show() },
                    shape = RoundedCornerShape(32.dp),
                    border = BorderStroke(1.dp, Secondary),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Select Date",
                        color = Primary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Button to open the TimePickerDialog.
                Button(
                    onClick = { timeDialogState.show() },
                    shape = RoundedCornerShape(32.dp),
                    border = BorderStroke(1.dp, Secondary),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Select Time",
                        color = Primary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Display the selected date/time if available.
                selectedDateTime?.let { dateTime ->
                    Text(
                        text = "Selected: ${dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Icon selection label.
                Text(
                    text = "Select an Icon",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )
                // Horizontal scrollable row for icon options.
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    iconOptions.forEach { icon ->
                        IconButton(onClick = { selectedIcon = icon }) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = icon,
                                contentDescription = "Icon",
                                tint = if (selectedIcon == icon) Primary else Secondary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            // Check if all required fields are complete.
            val allFieldsComplete = taskName.isNotBlank() &&
                    taskDescription.isNotBlank() &&
                    selectedIcon != null &&
                    selectedDateTime != null
            TextButton(
                onClick = {
                    if (!allFieldsComplete) return@TextButton

                    // Validate that the task name is alphanumeric.
                    if (!taskName.matches(Regex("^[a-zA-Z0-9 ]+\$"))) {
                        Toast.makeText(context, "Task name must be alphanumeric", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    // Validate that the task name does not exceed 20 characters.
                    if (taskName.length > 20) {
                        Toast.makeText(context, "Task name must not exceed 20 characters", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }

                    // Build the new TaskEntity using the selected icon's index.
                    val newTask = TaskEntity(
                        name = taskName,
                        description = taskDescription,
                        iconIndex = iconOptions.indexOf(selectedIcon!!),
                        createdDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        scheduledDateTime = selectedDateTime!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    )
                    onConfirm(newTask)
                },
                enabled = allFieldsComplete
            ) {
                Text(
                    text = "Save",
                    color = if (allFieldsComplete) Color.Black else Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    text = "Cancel",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )

    // --------------------------------------------------------------------
    // Date and Time Picker dialogs are reused from your DateTimePickers.kt file.
    // --------------------------------------------------------------------
    DatePickerDialog(
        dateDialogState = dateDialogState,
        initialDate = selectedDateTime?.toLocalDate() ?: LocalDate.now(),
        onDateSelected = { date ->
            // Combine the selected date with the current or default time.
            val currentTime = selectedDateTime?.toLocalTime() ?: LocalTime.now()
            selectedDateTime = LocalDateTime.of(date, currentTime)
            dateDialogState.hide()
        }
    )

    TimePickerDialog(
        timeDialogState = timeDialogState,
        onTimeSelected = { time ->
            // Combine the selected time with the current or default date.
            val currentDate = selectedDateTime?.toLocalDate() ?: LocalDate.now()
            selectedDateTime = LocalDateTime.of(currentDate, time)
            timeDialogState.hide()
        }
    )
}
