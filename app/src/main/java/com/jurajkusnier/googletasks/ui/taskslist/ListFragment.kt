package com.jurajkusnier.googletasks.ui.taskslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.data.TaskList
import com.jurajkusnier.googletasks.viewmodel.ViewModelFactory

abstract class TasksListFragment:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edit_list_fragment, container,false)
    }

    fun saveChanges(taskList: TaskList) {
        if (taskList.listName.isBlank()) return
        mViewModel.insertTaskList(taskList)
    }

    protected val mViewModel:ListFragmentViewModel by lazy {
        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        ViewModelProviders.of(this, viewModelFactory).get(ListFragmentViewModel::class.java)
    }

    abstract fun insertTasksList()
}