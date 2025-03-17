package com.alexismoraportal.todoapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alexismoraportal.todoapp.navigation.Screens
import com.alexismoraportal.todoapp.ui.components.AddTaskDialog
import com.alexismoraportal.todoapp.ui.components.BottomBarMenu
import com.alexismoraportal.todoapp.ui.components.EditTaskDialog
import com.alexismoraportal.todoapp.ui.components.SearchBar
import com.alexismoraportal.todoapp.ui.components.TaskList
import com.alexismoraportal.todoapp.viewmodel.TasksViewModel

/**
 * TasksScreen displays the list of tasks and manages dialogs for adding and editing tasks.
 *
 * It uses TasksViewModel to handle all UI state and business logic, including:
 *  - Managing the list of tasks.
 *  - Controlling the visibility of dialogs and the search bar.
 *  - Filtering tasks based on the search query.
 *  - Handling task addition, deletion, and updates with proper validation.
 *
 * @param navController Navigation controller to handle navigation events.
 * @param viewModel The ViewModel instance managing the tasks state (default provided by viewModel()).
 */
@Composable
fun TasksScreen(navController: NavController, viewModel: TasksViewModel = viewModel()) {
    val context = LocalContext.current

    // List of icon options to choose from when adding or editing tasks.
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
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
        bottomBar = {
            // BottomBarMenu provides navigation options including Add, Search, and Notifications.
            BottomBarMenu(
                onAddClick = { viewModel.updateShowAddDialog(true) },
                onSearchClick = { viewModel.toggleSearchBar() },
                onNotificationsClick = { navController.navigate(Screens.NotificationScreen.name) }
            )
        }
    ) { paddingValues ->
        // Main content column with smooth resizing using animateContentSize.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .animateContentSize(animationSpec = spring(dampingRatio = 0.8f, stiffness = 150f))
        ) {
            // Header / Title.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TODO APP",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
            }

            // Animated search bar that expands and contracts.
            AnimatedVisibility(
                visible = viewModel.showSearchBar,
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(animationSpec = tween(300))
            ) {
                SearchBar(
                    visible = true,
                    query = viewModel.searchQuery,
                    onQueryChanged = { viewModel.updateSearchQuery(it) }
                )
            }

            // Display the list of tasks. Tapping on a task opens the edit dialog,
            // and swiping or tapping delete removes the task.
            TaskList(
                tasks = viewModel.filteredTasks,
                onTaskClick = { task ->
                    viewModel.updateTaskToEdit(task)
                    viewModel.updateShowEditDialog(true)
                },
                onTaskDelete = { task ->
                    viewModel.deleteTask(task)
                }
            )

            // Add Task Dialog: Allows the user to add a new task.
            if (viewModel.showAddDialog) {
                AddTaskDialog(
                    showDialog = true,
                    iconOptions = iconOptions,
                    onConfirm = { newTask ->
                        viewModel.addTask(newTask, context)
                        viewModel.updateShowAddDialog(false)
                    },
                    onDismiss = { viewModel.updateShowAddDialog(false) }
                )
            }

            // Edit Task Dialog: Allows the user to edit an existing task.
            if (viewModel.showEditDialog && viewModel.taskToEdit != null) {
                EditTaskDialog(
                    showDialog = true,
                    task = viewModel.taskToEdit!!,
                    iconOptions = iconOptions,
                    onConfirm = { updatedTask ->
                        viewModel.updateTask(updatedTask, context)
                    },
                    onDismiss = {
                        viewModel.updateShowEditDialog(false)
                        viewModel.updateTaskToEdit(null)
                    }
                )
            }
        }
    }
}
