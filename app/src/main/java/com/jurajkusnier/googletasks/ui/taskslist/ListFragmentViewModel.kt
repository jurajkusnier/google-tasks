package com.jurajkusnier.googletasks.ui.taskslist

import androidx.lifecycle.ViewModel
import com.jurajkusnier.googletasks.data.TaskList
import com.jurajkusnier.googletasks.data.TasksListRepository

class ListFragmentViewModel(private val tasksListRepository: TasksListRepository): ViewModel() {
    var listNameLoaded:Boolean = false

    fun insertTaskList(taskList: TaskList) {
        tasksListRepository.insertTaskList(taskList)
    }

}