package com.jurajkusnier.googletasks.ui.taskslist

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.jurajkusnier.googletasks.db.TaskList
import kotlinx.android.synthetic.main.edit_list_fragment.*


class EditListFragment: TasksListFragment() {

    private lateinit var tasksList:TaskList

    companion object {
        val TAG = EditListFragment::class.java.simpleName
        const val LIST_ID  = "list_id"
        const val LIST_NAME  = "list_name"

        fun getInstance(taskList: TaskList):EditListFragment {
            val fragment = EditListFragment()
            val bundle = Bundle()
            bundle.putInt(LIST_ID,taskList.id)
            bundle.putString(LIST_NAME,taskList.listName)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel:EditListFragmentViewModel by lazy {
        ViewModelProviders.of(this).get(EditListFragmentViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        val listId = arguments?.getInt(LIST_ID,-1)
        val listName = arguments?.getString(LIST_NAME)
        if (listId == null || listName == null && listId < 0 ) {
            throw IllegalStateException("$TAG stared with wrong arguments (listId = $listId, listName = $listName)")
        }

        tasksList = TaskList(listId,listName!!)
        if (!viewModel.listNameLoaded) {
            //Load list name only once
            edit_list_title.setText(listName)
            viewModel.listNameLoaded = true
        }
    }

    override fun insertTasksList() {
        val listName = edit_list_title.text.toString()
        val listCopy = tasksList.copy(listName = listName)
        saveChanges(listCopy)
    }
}