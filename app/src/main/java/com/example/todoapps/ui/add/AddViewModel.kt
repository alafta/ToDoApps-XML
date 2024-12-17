package com.example.todoapps.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapps.data.model.ToDoItem
import com.example.todoapps.data.repository.ToDoRepository
import kotlinx.coroutines.launch

class AddViewModel (private val repository: ToDoRepository) : ViewModel() {

    private val _saveStatus = MutableLiveData<Boolean?>()
    val saveStatus: MutableLiveData<Boolean?> get() = _saveStatus

    fun addToDo (toDoItem: ToDoItem ) {
        viewModelScope.launch {
            try {
                repository.addToDo(toDoItem)
                _saveStatus.postValue(true)
            } catch (e: Exception) {
                _saveStatus.postValue(false)
            }
        }
    }

    fun resetSaveStatus() {
        _saveStatus.value = null
    }
}