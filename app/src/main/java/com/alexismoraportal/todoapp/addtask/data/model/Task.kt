package com.alexismoraportal.todoapp.addtask.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Task(
    val text: String,
    val icon: ImageVector,
    val createdDateTime: String,
    val scheduledDateTime: String
)
