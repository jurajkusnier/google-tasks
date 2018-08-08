package com.jurajkusnier.googletasks.di

import android.content.Context
import com.jurajkusnier.googletasks.db.AppDatabase
import com.jurajkusnier.googletasks.taskslist.TasksListRepository

object Injection {

    fun provideTaskListRespository(context: Context) : TasksListRepository {
        val database =AppDatabase.getInstance(context)
        return TasksListRepository.getInstance(database)
    }

}