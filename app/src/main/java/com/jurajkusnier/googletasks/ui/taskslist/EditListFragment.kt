package com.jurajkusnier.googletasks.ui.taskslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.db.TaskList
import com.jurajkusnier.googletasks.di.Injection
import kotlinx.android.synthetic.main.edit_list_fragment.*


class EditListFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edit_list_fragment, container,false)
    }

    fun insertTasksList() {
        val listName =  edit_list_title.text.toString()
        if (listName.isNotBlank()) {
            val appContext = context?.applicationContext ?: return
            val repository = Injection.provideTaskListRespository(appContext)
            repository.insertTaskList(TaskList(listName))
        }
    }
}