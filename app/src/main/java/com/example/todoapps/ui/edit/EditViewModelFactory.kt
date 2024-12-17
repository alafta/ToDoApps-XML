package com.example.todoapps.com.example.todoapps.ui.edit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapps.data.local.AppDatabase
import com.example.todoapps.data.repository.ToDoRepository
import com.example.todoapps.ui.edit.EditViewModel

class EditViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            // Get the database instance and repository
            val repository = ToDoRepository(AppDatabase.invoke(context).getToDoDao())
            return EditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}