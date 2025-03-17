package com.alexismoraportal.todoapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexismoraportal.todoapp.domain.TaskEntity
import com.alexismoraportal.todoapp.ui.theme.Primary
import com.alexismoraportal.todoapp.ui.theme.delete
import androidx.compose.material.icons.filled.*
import com.alexismoraportal.todoapp.utils.iconOptions

/**
 * TaskItem renders a single task card.
 *
 * It displays:
 * - The task icon (retrieved from a predefined icon list using the stored icon index).
 * - The task title (name) and its description.
 * - The creation and scheduled date/times.
 * - A delete icon that opens a confirmation dialog.
 * - A countdown for the scheduled time.
 *
 * @param task         The task data model.
 * @param modifier     Modifier for additional styling.
 * @param elevation    Elevation for the card.
 * @param textStyle    Text style for the task title.
 * @param onClick      Callback invoked when the task is clicked (for editing).
 * @param onDelete     Callback invoked when the task is confirmed for deletion.
 */
@Composable
fun TaskItem(
    task: TaskEntity,
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    onClick: () -> Unit,
    onDelete: () -> Unit  // Triggered only after user confirmation.
) {
    // Local state to control the visibility of the delete confirmation dialog.
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Retrieve the icon based on the stored icon index.
    val taskIcon: ImageVector = iconOptions.getOrElse(task.iconIndex) { Icons.Filled.Android }

    // Main card container displaying the task.
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(elevation),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Row containing the task icon, title, and delete icon.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Display the task icon.
                Icon(
                    imageVector = taskIcon,
                    contentDescription = "Task icon",
                    modifier = Modifier.size(28.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(12.dp))

                // Task title displaying the unique task name.
                Text(
                    text = task.name,
                    style = textStyle,
                    color = Color.Black
                )

                // Spacer to push the delete icon to the far right.
                Spacer(modifier = Modifier.weight(1f))

                // Delete icon that opens a confirmation dialog.
                IconButton(
                    modifier = Modifier.size(16.dp),
                    onClick = { showDeleteDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Delete task",
                        tint = delete
                    )
                }
            }

            // Display task description.
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Display creation and scheduled date/time.
            Column {
                Text(
                    text = "Created: ${task.createdDateTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Scheduled: ${task.scheduledDateTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Primary
                )
            }

            // Countdown text for the scheduled date/time.
            CountdownText(
                scheduledDateTimeString = task.scheduledDateTime,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    // Delete confirmation dialog.
    DeleteConfirmationDialog(
        showDialog = showDeleteDialog,
        onDismiss = { showDeleteDialog = false },
        onConfirm = {
            showDeleteDialog = false
            onDelete()
        }
    )
}
