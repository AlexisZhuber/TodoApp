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
import com.alexismoraportal.todoapp.utils.parseDateTime
import com.alexismoraportal.todoapp.ui.theme.Primary
import com.alexismoraportal.todoapp.ui.theme.Secondary
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * EditTaskDialog
 *
 * A composable dialog for editing an existing TaskEntity.
 * This dialog includes fields for:
 * - Editing the unique task name (must be alphanumeric and no more than 20 characters)
 * - Editing the task description
 * - Updating the scheduled date/time using external date and time pickers
 * - Icon selection from a list of provided options.
 *
 * The task's icon is represented by an index referring to the icon options list.
 *
 * @param showDialog Boolean flag to control dialog visibility.
 * @param task The TaskEntity object to be edited.
 * @param iconOptions List of available icon options.
 * @param onConfirm Lambda invoked with the updated TaskEntity when the user confirms.
 * @param onDismiss Lambda invoked when the dialog is dismissed.
 */
@Composable
fun EditTaskDialog(
    showDialog: Boolean,
    task: TaskEntity,
    iconOptions: List<ImageVector>,
    onConfirm: (TaskEntity) -> Unit,
    onDismiss: () -> Unit
) {
    // Obtain the current context for displaying Toast messages.
    val context = LocalContext.current

    // Exit early if dialog should not be shown.
    if (!showDialog) return

    // Local state for editing task name and description.
    var editedName by remember { mutableStateOf(task.name) }
    var editedDescription by remember { mutableStateOf(task.description) }

    // Local state for selected icon index (initialize from task.iconIndex).
    var editedIconIndex by remember { mutableStateOf(task.iconIndex) }

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
                    iconOptions.forEachIndexed { index, icon ->
                        IconButton(onClick = { editedIconIndex = index }) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = icon,
                                contentDescription = "Icon",
                                tint = if (editedIconIndex == index) Primary else Secondary
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
                        Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    // Validate that the task name is alphanumeric.
                    if (!editedName.matches(Regex("^[a-zA-Z0-9 ]+\$"))) {
                        Toast.makeText(context, "Task name must be alphanumeric", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    // Validate that the task name does not exceed 20 characters.
                    if (editedName.length > 20) {
                        Toast.makeText(context, "Task name must not exceed 20 characters", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    // Build the updated TaskEntity with the edited fields.
                    val updatedTask = task.copy(
                        name = editedName,
                        description = editedDescription,
                        iconIndex = editedIconIndex,
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

    // DatePickerDialog: Combines the selected date with the current time.
    DatePickerDialog(
        dateDialogState = dateDialogState,
        initialDate = editedDateTime.toLocalDate(),
        onDateSelected = { date ->
            val newDateTime = LocalDateTime.of(date, editedDateTime.toLocalTime())
            editedDateTime = if (newDateTime >= LocalDateTime.now()) newDateTime else LocalDateTime.now()
            dateDialogState.hide()
        }
    )

    // TimePickerDialog: Combines the selected time with the current date.
    TimePickerDialog(
        timeDialogState = timeDialogState,
        onTimeSelected = { time ->
            val newDateTime = LocalDateTime.of(editedDateTime.toLocalDate(), time)
            editedDateTime = if (newDateTime >= LocalDateTime.now()) newDateTime else LocalDateTime.now()
            timeDialogState.hide()
        }
    )
}
