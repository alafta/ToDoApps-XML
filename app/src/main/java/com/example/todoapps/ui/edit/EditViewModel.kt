package com.example.todoapps.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapps.data.model.ToDoItem
import com.example.todoapps.data.repository.ToDoRepository
import kotlinx.coroutines.launch

class EditViewModel (private val repository: ToDoRepository) : ViewModel(){
    fun editToDo (toDoItem: ToDoItem) =
        viewModelScope.launch {
            repository.editToDo(toDoItem)
        }

    fun deleteToDo(toDoItem: ToDoItem) {
        viewModelScope.launch {
            repository.deleteToDo(toDoItem)
        }
    }
}