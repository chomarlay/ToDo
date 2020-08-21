package com.noplanb.todo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noplanb.todo.data.models.ToDoData


@Dao
interface ToDoDao {

    @Query("SELECT * FROM ToDoData")
    fun getAllData(): LiveData<List<ToDoData>>

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData )
}