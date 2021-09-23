package com.masai.taskmanagerapp.models.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskappDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTasks(tasks: ArrayList<Task>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(task: Task)

    @Query("select * from tasks")
    fun getTasks() : LiveData<List<Task>>

    @Delete
    fun delete(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("delete from tasks")
    fun deleteAll()

}