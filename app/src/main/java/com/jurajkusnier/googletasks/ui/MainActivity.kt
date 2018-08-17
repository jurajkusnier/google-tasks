package com.jurajkusnier.googletasks.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.db.TaskList
import com.jurajkusnier.googletasks.ui.taskslist.AddListFragment
import com.jurajkusnier.googletasks.ui.taskslist.BottomSheetTasksList
import com.jurajkusnier.googletasks.ui.taskslist.EditListFragment
import com.jurajkusnier.googletasks.ui.taskslist.TasksListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        //Setup toolbars for currently loaded fragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment is TasksListFragment) {
            hideBottomAppBar()
            showTaskListToolbar(currentFragment)
        } else {
            showBottomAppBar()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                BottomSheetTasksList().show(supportFragmentManager, BottomSheetTasksList.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        toolbar.visibility = View.GONE
        showBottomAppBar()
    }


    fun showNewTaskListFragment() {
        val addListFragment = AddListFragment()
        showTaskListFragment(addListFragment )
    }

    fun showEditTaskListFragment(taskList: TaskList) {
        val editListFragment = EditListFragment.getInstance(taskList)
        showTaskListFragment(editListFragment)
    }

    private fun showTaskListFragment(tasksListFragment: TasksListFragment) {
        hideBottomAppBar()
        showTaskListToolbar(tasksListFragment)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, tasksListFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showTaskListToolbar(fragment:TasksListFragment) {
        toolbar.visibility = View.VISIBLE
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.task_list_edit)
        toolbar.title =  if (fragment is EditListFragment)
            getString(R.string.list_edit_title)
        else
            getString(R.string.list_add_title)

        toolbar.setNavigationIcon(R.drawable.ic_close)
        toolbar.setOnMenuItemClickListener {
            fragment.insertTasksList()
            onBackPressed()
            true
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun hideBottomAppBar() {
        bottom_app_bar_shadow.visibility = View.GONE
        bottomBar.visibility = View.GONE
    }

    private fun showBottomAppBar() {
        bottom_app_bar_shadow.visibility = View.VISIBLE
        bottomBar.visibility = View.VISIBLE
        setSupportActionBar(bottomBar)

        bottomBar.setOnMenuItemClickListener {
            when(it.itemId) {
                android.R.id.home -> {
                    BottomSheetTasksList().show(supportFragmentManager, BottomSheetTasksList.TAG)
                    true
                }
                else -> false
            }
        }
    }


}
