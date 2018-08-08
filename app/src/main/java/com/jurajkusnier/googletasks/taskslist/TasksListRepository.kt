package com.jurajkusnier.googletasks.taskslist

import com.jurajkusnier.googletasks.db.AppDatabase
import com.jurajkusnier.googletasks.db.TaskList
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class TasksListRepository private constructor(private val database:AppDatabase) {

    companion object {
        val TAG = TasksListRepository::class.java.simpleName

        @Volatile private var instance: TasksListRepository? = null

        fun getInstance(database:AppDatabase):TasksListRepository {

            return instance ?: synchronized(this) {
                instance = TasksListRepository(database)
                instance ?: throw IllegalAccessException("Can't instantiate class $TAG")
            }
        }
    }

    fun getTasksList(): Flowable<List<TaskList>>? {
        return database.getTaskListDao().getTaskLists()
                .observeOn(Schedulers.io())
    }

    fun insertTaskList(taskList: TaskList) {
        Completable.fromAction {
            database.getTaskListDao().insert(taskList)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun deleteTaskList(taskList: TaskList) {
        Completable.fromAction {
            database.getTaskListDao().delete(taskList)
        }.subscribeOn(Schedulers.io()).subscribe()
    }
}