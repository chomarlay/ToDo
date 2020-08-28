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

    @Query("SELECT * FROM ToDoData WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>>

    @Query("SELECT * FROM ToDoData ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<ToDoData>>

    @Query("SELECT * FROM ToDoData ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<ToDoData>>


}