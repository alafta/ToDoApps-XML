package com.example.todoapps.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = "todo_table")
@Parcelize
data class ToDoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Date,
): Parcelable
