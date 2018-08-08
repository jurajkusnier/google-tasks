package com.jurajkusnier.googletasks.ui.taskslist

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.viewmodel.ViewModelFactory

class BottomSheetTasksList: BottomSheetDialogFragment() {

    companion object {
        val TAG = BottomSheetTasksList::class.java.simpleName
    }

    private val viewModel:BottomSheetTasksListViewModel by lazy {
        val viewModelFactory = ViewModelFactory.getInstance(context?.applicationContext as Application)
        ViewModelProviders.of(this, viewModelFactory).get(BottomSheetTasksListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val navigationMenu = layoutInflater.inflate(R.layout.navigation_view_tasks_lists, container) as NavigationView

        viewModel.taskList.observe(this, Observer {

            val menu = navigationMenu.menu

            for (taskList in  it) {
                menu.add(taskList.listName)
            }

            navigationMenu.invalidate()
        })

        return navigationMenu
    }





}