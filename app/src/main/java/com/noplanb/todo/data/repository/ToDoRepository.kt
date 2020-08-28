package com.noplanb.todo.data.repository

import androidx.lifecycle.LiveData
import com.noplanb.todo.data.dao.ToDoDao
import com.noplanb.todo.data.models.ToDoData

class ToDoRepository (private val toDoDao: ToDoDao){

    val getAllData : LiveData<List<ToDoData>> = toDoDao.getAllData()
    val sortByLowPriority : LiveData<List<ToDoData>> = toDoDao.sortByLowPriority()
    val sortByHighPriority : LiveData<List<ToDoData>> = toDoDao.sortByHighPriority()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData:ToDoData) {
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData:ToDoData) {
        toDoDao.deleteItem(toDoData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>>{
        return toDoDao.searchDatabase(searchQuery)
    }

}