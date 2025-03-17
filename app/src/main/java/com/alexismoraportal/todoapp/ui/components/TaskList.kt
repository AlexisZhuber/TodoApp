package com.alexismoraportal.todoapp.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.alexismoraportal.todoapp.domain.TaskEntity

/**
 * TaskList.kt
 *
 * Displays a list of tasks in a LazyColumn and notifies when a task is clicked.
 * The LazyColumn is wrapped with animateContentSize() so that any layout changes
 * (like when the search bar appears or disappears) are smoothly animated.
 */
@Composable
fun TaskList(
    tasks: List<TaskEntity>,
    onTaskClick: (TaskEntity) -> Unit,
    onTaskDelete: (TaskEntity) -> Unit
) {
    LazyColumn{
        items(tasks) { task ->
            TaskItem(
                task = task,
                onClick = { onTaskClick(task) },
                onDelete = { onTaskDelete(task) }
            )
        }
    }
}
