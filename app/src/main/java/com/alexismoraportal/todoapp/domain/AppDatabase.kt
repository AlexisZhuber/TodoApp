package com.alexismoraportal.todoapp.domain

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * AppDatabase is the Room database for the application.
 * It includes TaskEntity and its corresponding DAO.
 *
 * This abstract class is extended by Room to create the actual database instance.
 */
@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
