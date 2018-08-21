package com.jurajkusnier.googletasks.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jurajkusnier.googletasks.SharedPreferencesHelper
import com.jurajkusnier.googletasks.di.Injection
import com.jurajkusnier.googletasks.ui.taskslist.BottomBarMenuViewModel
import com.jurajkusnier.googletasks.ui.taskslist.BottomSheetTasksListViewModel

class ViewModelFactory(private val context: Application): ViewModelProvider.Factory {

    companion object {
        val TAG = ViewModelFactory::class.java.simpleName

        @Volatile private var instance: ViewModelFactory? = null

        fun getInstance(context: Application):ViewModelFactory {
            return instance ?: synchronized(this) {
                instance = ViewModelFactory(context)
                instance ?: throw IllegalAccessException("Can't instantiate class $TAG")
            }
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BottomSheetTasksListViewModel::class.java)) {
            val tasksListRepository = Injection.provideTaskListRespository(context)
            return BottomSheetTasksListViewModel(tasksListRepository) as T
        }

        if (modelClass.isAssignableFrom(BottomBarMenuViewModel::class.java)) {
            val tasksListRepository = Injection.provideTaskListRespository(context)
            val preferencesHelper = SharedPreferencesHelper.getInstance(context)
            return BottomBarMenuViewModel(tasksListRepository, preferencesHelper) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}