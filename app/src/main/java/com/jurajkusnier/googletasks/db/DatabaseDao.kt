package com.jurajkusnier.googletasks.db

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM Task WHERE TASK_LIST_ID = :taskListId")
    fun findTasksFromList(taskListId:Int):Flowable<List<Task>>

    @Query("SELECT * FROM Task")
    fun listAllTasks():Flowable<List<Task>>

}

@Dao
interface TaskListDao {
    @Query("SELECT * FROM TaskList")
    fun getTaskLists():Flowable<List<TaskList>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskList: TaskList)

    @Delete
    fun delete(taskList: TaskList)
}

@Dao
interface SubtaskDao {
    @Query("SELECT * FROM Subtask WHERE TASK_ID= :taskId")
    fun findSubtasksFromTask(taskId:Int):Flowable<List<Subtask>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(subtask: Subtask)

    @Delete
    fun delete(subtask: Subtask)
}


