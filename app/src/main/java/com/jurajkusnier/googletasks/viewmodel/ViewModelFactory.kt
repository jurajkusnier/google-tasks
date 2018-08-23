package com.jurajkusnier.googletasks.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jurajkusnier.googletasks.data.PreferencesDataStore
import com.jurajkusnier.googletasks.data.TasksListRepository
import com.jurajkusnier.googletasks.di.Injection
import com.jurajkusnier.googletasks.ui.activity.MainActivityViewModel
import com.jurajkusnier.googletasks.ui.bottombarmenu.BottomBarMenuViewModel
import com.jurajkusnier.googletasks.ui.bottomdrawer.BottomDrawerTasksListViewModel
import com.jurajkusnier.googletasks.ui.tasks.TasksViewModel
import com.jurajkusnier.googletasks.ui.taskslist.ListFragmentViewModel

class ViewModelFactory(
        private val tasksListRepository:TasksListRepository,
        private val preferencesDataStore: PreferencesDataStore
        ): ViewModelProvider.Factory {

    companion object {
        val TAG = ViewModelFactory::class.java.simpleName

        @Volatile private var instance: ViewModelFactory? = null

        fun getInstance(context: Context):ViewModelFactory {
            return instance ?: synchronized(this) {

                val tasksListRepository = Injection.provideTaskListRespository(context)
                val preferencesDataStore = PreferencesDataStore.getInstance(context)

                instance = ViewModelFactory(tasksListRepository,preferencesDataStore)
                instance ?: throw IllegalAccessException("Can't instantiate class $TAG")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BottomDrawerTasksListViewModel::class.java)) {
            return BottomDrawerTasksListViewModel(tasksListRepository, preferencesDataStore) as T
        }

        if (modelClass.isAssignableFrom(BottomBarMenuViewModel::class.java)) {
            return BottomBarMenuViewModel(tasksListRepository, preferencesDataStore) as T
        }

        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(tasksListRepository, preferencesDataStore) as T
        }

        if (modelClass.isAssignableFrom(ListFragmentViewModel::class.java)) {
            return ListFragmentViewModel(tasksListRepository) as T
        }

        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(tasksListRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}