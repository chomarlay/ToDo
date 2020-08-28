package com.noplanb.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.noplanb.todo.data.ToDoDatabase
import com.noplanb.todo.data.dao.ToDoDao
import com.noplanb.todo.data.models.ToDoData
import com.noplanb.todo.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private val toDoDao: ToDoDao = ToDoDatabase.getDatabase(application).toDoDao()

    private val repository: ToDoRepository

    val getAllData: LiveData<List<ToDoData>>
    val sortByLowPriority: LiveData<List<ToDoData>>
    val sortByHighPriority: LiveData<List<ToDoData>>

    init {
        repository = ToDoRepository(toDoDao)
        getAllData = repository.getAllData
        sortByLowPriority = repository.sortByLowPriority
        sortByHighPriority = repository.sortByHighPriority

    }

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertData(toDoData) }
    }

    fun updateData(toDoData: ToDoData) {
        viewModelScope.launch( Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoData) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>>{
        return repository.searchDatabase(searchQuery)
    }

}