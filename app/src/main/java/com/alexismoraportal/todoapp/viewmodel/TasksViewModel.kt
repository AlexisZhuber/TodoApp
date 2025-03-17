package com.alexismoraportal.todoapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alexismoraportal.todoapp.model.Task
import com.alexismoraportal.todoapp.util.parseDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * TasksViewModel manages the state for the tasks screen including the list of tasks,
 * dialog visibility, search query, and task filtering.
 *
 * It also contains business logic for adding, updating, and deleting tasks with validation.
 * Hilt is used to inject dependencies into this ViewModel.
 */
@HiltViewModel
class TasksViewModel @Inject constructor() : ViewModel() {

    // Formatter for date and time in "dd/MM/yyyy HH:mm" format.
    private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    // A mutable state list holding the tasks.
    // Initially, two sample tasks are added to the list.
    private val _tasks = mutableStateListOf(
        Task(
            name = "SampleTask1",
            description = "This is the first sample task.",
            icon = Icons.Filled.Star,
            createdDateTime = LocalDateTime.now().format(dateFormat),
            scheduledDateTime = LocalDateTime.now().plusHours(0).format(dateFormat)
        ),
        Task(
            name = "SampleTask2",
            description = "This is the second sample task.",
            icon = Icons.Filled.Android,
            createdDateTime = LocalDateTime.now().format(dateFormat),
            scheduledDateTime = LocalDateTime.now().plusDays(1).format(dateFormat)
        )
    )

    // Public getter for tasks, exposing the mutable state list as an immutable list.
    val tasks: List<Task>
        get() = _tasks

    // UI state variables:

    // Controls the visibility of the "Add Task" dialog.
    var showAddDialog by mutableStateOf(false)
        private set

    // Controls the visibility of the "Edit Task" dialog.
    var showEditDialog by mutableStateOf(false)
        private set

    // Holds the task that is currently being edited.
    var taskToEdit: Task? by mutableStateOf(null)
        private set

    // Controls the visibility of the search bar.
    var showSearchBar by mutableStateOf(false)
        private set

    // Holds the current search query.
    var searchQuery by mutableStateOf("")
        private set

    // Computed property to filter tasks based on the search query.
    val filteredTasks: List<Task>
        get() = if (searchQuery.isNotBlank())
            _tasks.filter { it.name.contains(searchQuery, ignoreCase = true) }
        else _tasks

    /**
     * Toggles the visibility of the search bar.
     */
    fun toggleSearchBar() {
        showSearchBar = !showSearchBar
    }

    /**
     * Updates the search query state.
     *
     * @param query The new search query string.
     */
    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    /**
     * Updates the visibility state of the "Add Task" dialog.
     *
     * @param show Boolean indicating if the dialog should be visible.
     */
    fun updateShowAddDialog(show: Boolean) {
        showAddDialog = show
    }

    /**
     * Updates the visibility state of the "Edit Task" dialog.
     *
     * @param show Boolean indicating if the dialog should be visible.
     */
    fun updateShowEditDialog(show: Boolean) {
        showEditDialog = show
    }

    /**
     * Updates the task that is currently being edited.
     *
     * @param task The task to edit or null to reset.
     */
    fun updateTaskToEdit(task: Task?) {
        taskToEdit = task
    }

    /**
     * Adds a new task to the list after performing validations.
     *
     * Validations:
     *  - The task name must be alphanumeric.
     *  - The task name must not exceed 20 characters.
     *  - The task name must be unique (case insensitive).
     *  - The scheduled date/time must not be in the past.
     *
     * @param newTask The new Task object to be added.
     * @param context The context to display Toast messages.
     */
    fun addTask(newTask: Task, context: Context) {
        when {
            !newTask.name.matches(Regex("^[a-zA-Z0-9 ]+\$")) -> {
                Toast.makeText(context, "Task name must be alphanumeric", Toast.LENGTH_SHORT).show()
                return
            }
            newTask.name.length > 20 -> {
                Toast.makeText(context, "Task name must not exceed 20 characters", Toast.LENGTH_SHORT).show()
                return
            }
            _tasks.any { it.name.equals(newTask.name, ignoreCase = true) } -> {
                Toast.makeText(context, "Task name must be unique", Toast.LENGTH_SHORT).show()
                return
            }
            parseDateTime(newTask.scheduledDateTime).isBefore(LocalDateTime.now()) -> {
                Toast.makeText(context, "Selected date/time cannot be in the past", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                _tasks.add(newTask)
            }
        }
    }

    /**
     * Deletes the given task from the list.
     *
     * @param task The Task object to be removed.
     */
    fun deleteTask(task: Task) {
        _tasks.remove(task)
    }

    /**
     * Updates an existing task with new details after performing validations.
     *
     * Validations:
     *  - The new task name must be unique (case insensitive) compared to other tasks.
     *  - The task name must be alphanumeric.
     *  - The task name must not exceed 20 characters.
     *
     * @param updatedTask The updated Task object.
     * @param context The context to display Toast messages.
     */
    fun updateTask(updatedTask: Task, context: Context) {
        when {
            _tasks.any { it.name.equals(updatedTask.name, ignoreCase = true) && it != taskToEdit } -> {
                Toast.makeText(context, "Task name must be unique", Toast.LENGTH_SHORT).show()
                return
            }
            !updatedTask.name.matches(Regex("^[a-zA-Z0-9 ]+\$")) -> {
                Toast.makeText(context, "Task name must be alphanumeric", Toast.LENGTH_SHORT).show()
                return
            }
            updatedTask.name.length > 20 -> {
                Toast.makeText(context, "Task name must not exceed 20 characters", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                val index = _tasks.indexOfFirst { it == taskToEdit }
                if (index != -1) {
                    _tasks[index] = updatedTask
                    // Reset editing state.
                    showEditDialog = false
                    taskToEdit = null
                }
            }
        }
    }
}
