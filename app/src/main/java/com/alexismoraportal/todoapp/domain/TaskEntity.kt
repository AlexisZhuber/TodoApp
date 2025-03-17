package com.alexismoraportal.todoapp.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data Entity representing a Task for Room persistence.
 * This entity is part of the domain layer.
 *
 * Note:
 *  - We added an auto-generated primary key 'id' required by Room.
 *  - The icon is stored as an integer resource id (iconRes) because Room does not support storing ImageVector.
 *
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val iconIndex: Int, // Index for the selected icon from the predefined list
    val createdDateTime: String,
    val scheduledDateTime: String
)
