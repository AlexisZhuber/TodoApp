package com.alexismoraportal.todoapp.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for task-related operations.
 * This abstracts the data layer from the ViewModel.
 */
interface TaskRepository {
    fun getTasks(): Flow<List<TaskEntity>>
    suspend fun insertTask(task: TaskEntity)
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
}

/**
 * Concrete implementation of TaskRepository.
 *
 * It is annotated with @Singleton to ensure that a single instance is provided via dependency injection (Hilt).
 *
 * Using coroutines here allows non-blocking database operations, which is a practical improvement:
 *  - Database operations run in the background and do not block the main UI thread.
 *  - Structured concurrency in coroutines helps in managing cancellation and errors.
 */
@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()

    override suspend fun insertTask(task: TaskEntity) = taskDao.insertTask(task)

    override suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)

    override suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)
}
