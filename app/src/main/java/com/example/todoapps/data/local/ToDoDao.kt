package com.example.todoapps.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapps.data.model.ToDoItem

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo_table ORDER BY dueDate ASC")
    fun getAllToDos(): LiveData<List<ToDoItem>>

    @Query("SELECT * FROM todo_table WHERE title LIKE :query ORDER BY dueDate ASC")
    suspend fun searchToDo(query: String): List<ToDoItem>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToDo(todo: ToDoItem)

    @Update
    suspend fun editToDo(todo: ToDoItem)

    @Delete
    suspend fun deleteToDo(todo: ToDoItem)
}