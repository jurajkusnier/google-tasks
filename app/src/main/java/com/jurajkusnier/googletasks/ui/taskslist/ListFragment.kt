package com.jurajkusnier.googletasks.ui.taskslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.db.TaskList
import com.jurajkusnier.googletasks.di.Injection

abstract class TasksListFragment:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edit_list_fragment, container,false)
    }

    fun saveChanges(taskList: TaskList) {
        if (taskList.listName.isBlank()) return

        val appContext = context?.applicationContext ?: return
        val repository = Injection.provideTaskListRespository(appContext)
        repository.insertTaskList(taskList)
    }

    abstract fun insertTasksList()
}