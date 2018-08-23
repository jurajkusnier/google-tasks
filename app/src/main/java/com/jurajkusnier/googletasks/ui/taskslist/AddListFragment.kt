package com.jurajkusnier.googletasks.ui.taskslist

import com.jurajkusnier.googletasks.data.TaskList
import kotlinx.android.synthetic.main.edit_list_fragment.*

class AddListFragment:TasksListFragment() {

    override fun insertTasksList() {
        val listName =  edit_list_title.text.toString()
        saveChanges(TaskList(listName))
    }
}