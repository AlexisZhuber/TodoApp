package com.alexismoraportal.todoapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * DeleteConfirmationDialog.kt
 *
 * A reusable dialog composable that asks the user to confirm or cancel a delete action.
 * It follows the Single Responsibility Principle by focusing solely on the confirmation logic.
 */

@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!showDialog) return

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Confirm Deletion")
        },
        text = {
            Text(text = "Are you sure you want to delete this task?")
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text = "Yes, delete", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
            }
        }
    )
}
