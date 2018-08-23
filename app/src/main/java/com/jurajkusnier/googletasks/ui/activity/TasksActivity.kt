package com.jurajkusnier.googletasks.ui.activity

import com.jurajkusnier.googletasks.data.TaskList

interface TasksActivity {

    fun showUndoSnackbar(taskList: TaskList)

    fun showNewTaskListFragment()
    fun showEditTaskListFragment(taskList: TaskList)
}