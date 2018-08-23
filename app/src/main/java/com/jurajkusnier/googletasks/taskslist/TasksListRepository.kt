package com.jurajkusnier.googletasks.taskslist

import com.jurajkusnier.googletasks.SharedPreferencesHelper
import com.jurajkusnier.googletasks.db.AppDatabase
import com.jurajkusnier.googletasks.db.TaskList
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class TasksListRepository private constructor(private val database:AppDatabase, private val sharedPreferencesHelper: SharedPreferencesHelper) {

    companion object {
        val TAG = TasksListRepository::class.java.simpleName

        @Volatile private var instance: TasksListRepository? = null

        fun getInstance(database:AppDatabase, sharedPreferencesHelper: SharedPreferencesHelper):TasksListRepository {

            return instance ?: synchronized(this) {
                instance = TasksListRepository(database,sharedPreferencesHelper)
                instance ?: throw IllegalAccessException("Can't instantiate class $TAG")
            }
        }
    }

    fun findTasksList(id:Int): Observable<TaskList> {
        return database.getTaskListDao().findTaskListsById(id)
                .subscribeOn(Schedulers.io())
    }

    fun getTasksList(): Flowable<List<TaskList>>? {
        return database.getTaskListDao().getTaskLists()
                .observeOn(Schedulers.io())
    }

    fun insertTaskList(taskList: TaskList) {
        Completable.fromAction {
            val rowId = database.getTaskListDao().insert(taskList)
            sharedPreferencesHelper.selectedTaskList = rowId.toInt()
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun deleteTaskList(taskList: TaskList) {
        Completable.fromAction {
            database.getTaskListDao().delete(taskList)
        }.subscribeOn(Schedulers.io()).subscribe()
    }
}