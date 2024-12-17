package com.example.todoapps.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapps.data.repository.ToDoRepository

class AddViewModelFactory(private val repository: ToDoRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}