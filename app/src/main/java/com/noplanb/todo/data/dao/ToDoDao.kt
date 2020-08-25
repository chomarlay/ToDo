package com.noplanb.todo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*

import com.noplanb.todo.data.models.ToDoData


@Dao
interface ToDoDao {

    @Query("SELECT * FROM ToDoData")
    fun getAllData(): LiveData<List<ToDoData>>

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData )

    @Update
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    suspend fun deleteItem(toDoData: ToDoData)

    @Query("DELETE FROM ToDoData")
    suspend fun deleteAll()

}