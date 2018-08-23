package com.jurajkusnier.googletasks.di

import android.content.Context
import com.jurajkusnier.googletasks.data.PreferencesDataStore
import com.jurajkusnier.googletasks.data.AppDatabase
import com.jurajkusnier.googletasks.data.TasksListRepository

object Injection {

    fun provideTaskListRespository(context: Context) : TasksListRepository {
        val database = AppDatabase.getInstance(context)
        val sharedPreferencesHelper = PreferencesDataStore.getInstance(context)
        return TasksListRepository.getInstance(database,sharedPreferencesHelper)
    }

}