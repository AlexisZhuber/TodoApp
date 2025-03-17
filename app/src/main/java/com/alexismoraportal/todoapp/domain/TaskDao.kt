package com.alexismoraportal.todoapp.domain

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for tasks.
 * Defines the database operations for TaskEntity.
 *
 * All database operations are defined as suspend functions or return a Flow,
 * which ensures asynchronous execution using Kotlin coroutines.
 */
@Dao
interface TaskDao {
    /**
     * Inserts a new task into the database.
     * This function is marked as suspend to run in a coroutine.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTask(task: TaskEntity)

    /**
     * Updates an existing task.
     */
    @Update
    suspend fun updateTask(task: TaskEntity)

    /**
     * Deletes a task from the database.
     */
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    /**
     * Retrieves all tasks from the database as a Flow.
     * Flow ensures that any changes in the database are emitted asynchronously.
     */
    @Query("SELECT * FROM tasks ORDER BY createdDateTime DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>
}
