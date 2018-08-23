package com.jurajkusnier.googletasks.ui.activity

import androidx.lifecycle.ViewModel
import com.jurajkusnier.googletasks.data.TaskList
import com.jurajkusnier.googletasks.data.TasksListRepository

class MainActivityViewModel(private val tasksListRepository: TasksListRepository):ViewModel() {

    fun insertTaskList(taskList: TaskList) {
        tasksListRepository.insertTaskList(taskList)
    }
}