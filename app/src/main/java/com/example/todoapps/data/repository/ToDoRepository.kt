package com.example.todoapps.data.repository

import androidx.lifecycle.LiveData
import com.example.todoapps.data.local.ToDoDao
import com.example.todoapps.data.model.ToDoItem

class ToDoRepository(private val toDoDao: ToDoDao) {
    val allToDos: LiveData<List<ToDoItem>> = toDoDao.getAllToDos()

    suspend fun addToDo(todo: ToDoItem) {
        toDoDao.addToDo(todo)
    }
    suspend fun editToDo(todo: ToDoItem) {
        toDoDao.editToDo(todo)
    }
    suspend fun deleteToDo(todo: ToDoItem) {
        toDoDao.deleteToDo(todo)
    }
    suspend fun searchToDo(query: String): List<ToDoItem> {
        return toDoDao.searchToDo(query)
    }
}

