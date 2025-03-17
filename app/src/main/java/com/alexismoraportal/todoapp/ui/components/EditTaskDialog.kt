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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexismoraportal.todoapp.model.Task
import com.alexismoraportal.todoapp.util.parseDateTime
import com.alexismoraportal.todoapp.ui.theme.Primary
import com.alexismoraportal.todoapp.ui.theme.Secondary
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.alexismoraportal.todoapp.ui.components.DatePickerDialog
import com.alexismoraportal.todoapp.ui.components.TimePickerDialog

/**
 * EditTaskDialog
 *
 * A composable dialog for editing an existing Task. This dialog includes separate fields for:
 * - Editing the unique task name (must be alphanumeric and no more than 20 characters)
 * - Editing the task description
 * - Updating the scheduled date/time using external date and time pickers
 * - Icon selection from a list of provided options.
 */
@Composable
fun EditTaskDialog(
    showDialog: Boolean,
    task: Task?,
    iconOptions: List<ImageVector>,
    onConfirm: (Task) -> Unit,
    onDismiss: () -> Unit
) {
    if (!showDialog || task == null) return

    // Local states for editing task name, description, and icon.
    var editedName by remember { mutableStateOf(task.name) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var editedIcon by remember { mutableStateOf(task.icon) }

    // Parse the original scheduled date/time string.
    var editedDateTime by remember { mutableStateOf(parseDateTime(task.scheduledDateTime)) }

    // States for Date and Time pickers.
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "EDIT TASK",
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                // Outlined text field for editing the task name.
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { if (it.length <= 20) editedName = it },
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
                // Outlined text field for editing the task description.
                OutlinedTextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
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
                // Display the selected date/time.
                Text(
                    text = "Scheduled: ${editedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
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
                        IconButton(onClick = { editedIcon = icon }) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = icon,
                                contentDescription = "Icon",
                                tint = if (editedIcon == icon) Primary else Secondary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Validate that the task name is not blank.
                    if (editedName.isBlank()) {
                        Toast.makeText(null, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    // Validate that the task name is alphanumeric.
                    if (!editedName.matches(Regex("^[a-zA-Z0-9 ]+\$"))) {
                        Toast.makeText(null, "Task name must be alphanumeric", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    // Validate that the task name does not exceed 20 characters.
                    if (editedName.length > 20) {
                        Toast.makeText(null, "Task name must not exceed 20 characters", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    // Build the updated Task with the edited fields.
                    val updatedTask = task.copy(
                        name = editedName,
                        description = editedDescription,
                        icon = editedIcon,
                        scheduledDateTime = editedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    )
                    onConfirm(updatedTask)
                }
            ) {
                Text(
                    text = "Update",
                    color = Color.Black,
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
    // Date and Time picker dialogs reused from your DateTimePickers.kt file.
    // --------------------------------------------------------------------
    DatePickerDialog(
        dateDialogState = dateDialogState,
        initialDate = editedDateTime.toLocalDate(),
        onDateSelected = { date ->
            // Combine the selected date with the existing time.
            val newDateTime = LocalDateTime.of(date, editedDateTime.toLocalTime())
            editedDateTime = if (newDateTime >= LocalDateTime.now()) newDateTime else LocalDateTime.now()
            dateDialogState.hide()
        }
    )

    TimePickerDialog(
        timeDialogState = timeDialogState,
        onTimeSelected = { time ->
            // Combine the selected time with the existing date.
            val newDateTime = LocalDateTime.of(editedDateTime.toLocalDate(), time)
            editedDateTime = if (newDateTime >= LocalDateTime.now()) newDateTime else LocalDateTime.now()
            timeDialogState.hide()
        }
    )
}
