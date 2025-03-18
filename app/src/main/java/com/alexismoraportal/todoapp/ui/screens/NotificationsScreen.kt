package com.alexismoraportal.todoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexismoraportal.todoapp.domain.TaskEntity
import com.alexismoraportal.todoapp.utils.iconOptions
import com.alexismoraportal.todoapp.utils.parseDateTime
import com.alexismoraportal.todoapp.ui.theme.Primary
import com.alexismoraportal.todoapp.viewmodel.TasksViewModel
import java.time.LocalDateTime

/**
 * NotificationScreen displays tasks that are due within the next hour.
 * Tasks are shown in a LazyColumn with each task represented in a Card.
 *
 * @param navController NavController to handle navigation.
 * @param viewModel The TasksViewModel instance injected via Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: TasksViewModel = hiltViewModel()
) {
    // Compute tasks due within the next hour
    val dueSoonTasks by remember {
        derivedStateOf {
            viewModel.tasks.filter { task ->
                val scheduledTime = parseDateTime(task.scheduledDateTime)
                scheduledTime.isAfter(LocalDateTime.now()) &&
                        scheduledTime.isBefore(LocalDateTime.now().plusHours(1))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Notifications") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Navigate back to Home screen
                        navController.navigate("Home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = Modifier.background(Color.White),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (dueSoonTasks.isEmpty()) {
            // Display message if no tasks are due soon
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tasks due within the next hour",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(dueSoonTasks) { task ->
                        TaskNotificationCard(task = task)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

/**
 * TaskNotificationCard displays a single task in a Card layout.
 *
 * @param task The TaskEntity to display.
 */
@Composable
fun TaskNotificationCard(task: TaskEntity) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display the task icon based on the icon index.
            // Use a default icon if the index is out of bounds.
            Icon(
                imageVector = iconOptions.getOrElse(task.iconIndex) { Icons.Default.List },
                contentDescription = "Task Icon",
                tint = Primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Display the task name.
            Text(
                text = task.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
