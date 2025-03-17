package com.alexismoraportal.todoapp.di

import android.content.Context
import androidx.room.Room
import com.alexismoraportal.todoapp.domain.AppDatabase
import com.alexismoraportal.todoapp.domain.TaskDao
import com.alexismoraportal.todoapp.domain.TaskRepository
import com.alexismoraportal.todoapp.domain.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule provides dependencies related to the Room database,
 * DAO, and repository implementation.
 *
 * By creating this module, we follow the dependency inversion principle,
 * and Hilt can inject these dependencies wherever needed (e.g., in ViewModels).
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the Room Database instance.
     *
     * @param context The application context.
     * @return A singleton instance of AppDatabase.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "todo_app_database"
        ).build()
    }

    /**
     * Provides the TaskDao instance from the database.
     *
     * @param database The Room database instance.
     * @return The TaskDao instance.
     */
    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()

    /**
     * Provides the TaskRepository implementation.
     *
     * @param taskDao The DAO instance for tasks.
     * @return The TaskRepository implementation.
     */
    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository = TaskRepositoryImpl(taskDao)
}
