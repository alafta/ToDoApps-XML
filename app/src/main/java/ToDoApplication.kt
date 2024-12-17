package com.example.todoapps

import android.app.Application
import com.example.todoapps.data.local.AppDatabase
import com.example.todoapps.data.repository.ToDoRepository

class ToDoApplication : Application() {
    private val database: AppDatabase by lazy { AppDatabase.invoke(this) }
    val repository: ToDoRepository by lazy { ToDoRepository(database.getToDoDao()) }
}
