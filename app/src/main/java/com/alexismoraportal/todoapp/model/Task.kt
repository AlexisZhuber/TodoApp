package com.alexismoraportal.todoapp.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data model representing a Task.
 *
 * @param name             The unique alphanumeric title of the task.
 *                         Must not exceed 20 characters.
 * @param description      A detailed description of the task.
 * @param icon             An icon representing the task.
 * @param createdDateTime  The date and time when the task was created.
 * @param scheduledDateTime The date and time when the task is scheduled.
 */
data class Task(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val createdDateTime: String,
    val scheduledDateTime: String
)
