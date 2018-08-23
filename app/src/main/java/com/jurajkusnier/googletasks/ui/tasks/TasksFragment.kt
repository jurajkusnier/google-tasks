package com.jurajkusnier.googletasks.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.tasks_fragment_layout.*

class TasksFragment:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tasks_fragment_layout, container,false)
    }

    private val viewModel by lazy {
        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        ViewModelProviders.of(this,viewModelFactory).get(TasksViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        viewModel.currentTaskList.observe(viewLifecycleOwner, Observer {
            task_list_name.text = it.listName
        })

    }
}