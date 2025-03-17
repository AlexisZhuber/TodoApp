package com.alexismoraportal.todoapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexismoraportal.todoapp.domain.TaskEntity
import com.alexismoraportal.todoapp.domain.TaskRepository
import com.alexismoraportal.todoapp.utils.parseDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * TasksViewModel manages the state for the tasks screen.
 * It communicates with the TaskRepository to perform CRUD operations on tasks.
 *
 * By using coroutines, we ensure that database operations run on background threads,
 * which prevents UI thread blocking and improves performance.
 *
 * SOLID Principles applied:
 *  - Single Responsibility: The ViewModel handles only UI-related logic.
 *  - Dependency Inversion: It depends on the abstract TaskRepository rather than concrete implementations.
 *
 * MVVM Architecture:
 *  - The ViewModel holds the UI state and interacts with the data layer via the repository.
 */
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    // Formatter for date and time in "dd/MM/yyyy HH:mm" format.
    private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    // UI state variables controlling dialog visibility and search query.
    var showAddDialog = mutableStateOf(false)
        private set

    var showEditDialog = mutableStateOf(false)
        private set

    var taskToEdit = mutableStateOf<TaskEntity?>(null)
        private set

    var showSearchBar = mutableStateOf(false)
        private set

    var searchQuery = mutableStateOf("")
        private set

    // List of tasks fetched from the repository.
    var tasks by mutableStateOf<List<TaskEntity>>(emptyList())
        private set

    /**
     * Initialization block collects tasks from the repository.
     *
     * Using a coroutine (launched in viewModelScope) ensures that we continuously listen
     * to the Flow provided by Room. Any changes in the database will be emitted and
     * the UI will be updated accordingly.
     */
    init {
        viewModelScope.launch {
            repository.getTasks().collect { taskList ->
                tasks = taskList
            }
        }
    }

    /**
     * Computed property to filter tasks based on the search query.
     */
    val filteredTasks: List<TaskEntity>
        get() = if (searchQuery.value.isNotBlank())
            tasks.filter { it.name.contains(searchQuery.value, ignoreCase = true) }
        else tasks

    fun toggleSearchBar() {
        showSearchBar.value = !showSearchBar.value
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun updateShowAddDialog(show: Boolean) {
        showAddDialog.value = show
    }

    fun updateShowEditDialog(show: Boolean) {
        showEditDialog.value = show
    }

    fun updateTaskToEdit(task: TaskEntity?) {
        taskToEdit.value = task
    }

    /**
     * Adds a new task to the database after performing validations.
     *
     * This function uses a coroutine to perform the database operation asynchronously.
     * Launching a coroutine in viewModelScope ensures that the operation does not block the UI thread.
     *
     * @param newTask The new TaskEntity to be added.
     * @param context The context to display Toast messages.
     */
    fun addTask(newTask: TaskEntity, context: Context) {
        viewModelScope.launch {
            when {
                !newTask.name.matches(Regex("^[a-zA-Z0-9 ]+\$")) -> {
                    Toast.makeText(context, "Task name must be alphanumeric", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                newTask.name.length > 20 -> {
                    Toast.makeText(context, "Task name must not exceed 20 characters", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                tasks.any { it.name.equals(newTask.name, ignoreCase = true) } -> {
                    Toast.makeText(context, "Task name must be unique", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                parseDateTime(newTask.scheduledDateTime).isBefore(LocalDateTime.now()) -> {
                    Toast.makeText(context, "Selected date/time cannot be in the past", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                else -> {
                    repository.insertTask(newTask)
                }
            }
        }
    }

    /**
     * Deletes the given task from the database asynchronously.
     *
     * @param task The TaskEntity to be removed.
     */
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    /**
     * Updates an existing task in the database after validations.
     *
     * The coroutine ensures that the update runs in the background.
     *
     * @param updatedTask The updated TaskEntity.
     * @param context The context to display Toast messages.
     */
    fun updateTask(updatedTask: TaskEntity, context: Context) {
        viewModelScope.launch {
            when {
                tasks.any { it.name.equals(updatedTask.name, ignoreCase = true) && it.id != taskToEdit.value?.id } -> {
                    Toast.makeText(context, "Task name must be unique", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                !updatedTask.name.matches(Regex("^[a-zA-Z0-9 ]+\$")) -> {
                    Toast.makeText(context, "Task name must be alphanumeric", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                updatedTask.name.length > 20 -> {
                    Toast.makeText(context, "Task name must not exceed 20 characters", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                else -> {
                    repository.updateTask(updatedTask)
                    // Reset editing state after a successful update.
                    showEditDialog.value = false
                    taskToEdit.value = null
                }
            }
        }
    }
}
