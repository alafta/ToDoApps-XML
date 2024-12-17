package com.example.todoapps.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapps.data.model.ToDoItem
import com.example.todoapps.data.repository.ToDoRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ToDoRepository): ViewModel() {

    val allToDos: LiveData<List<ToDoItem>> = repository.allToDos

    private val _searchResults = MutableLiveData<List<ToDoItem>>()
    val searchResults: LiveData<List<ToDoItem>> get() = _searchResults

    private val _expandedItems = MutableLiveData<MutableSet<Int>>(mutableSetOf())
    val expandedItems: LiveData<MutableSet<Int>> = _expandedItems

    fun updateExpandedItems(expandedList: Set<Int>) {
        _expandedItems.value = expandedList.toMutableSet()
    }

    fun getExpandedItems(): Set<Int> {
        return _expandedItems.value ?: emptySet()
    }

    fun deleteToDo(toDoItem: ToDoItem) {
        viewModelScope.launch {
            repository.deleteToDo(toDoItem)
        }
    }

    fun searchToDo(query: String) {
        viewModelScope.launch {
            val results = repository.searchToDo(query)
            _searchResults.postValue(results)
        }
    }
}


