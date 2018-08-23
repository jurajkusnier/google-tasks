package com.jurajkusnier.googletasks.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.data.TaskList
import com.jurajkusnier.googletasks.ui.bottombarmenu.BottomBarMenu
import com.jurajkusnier.googletasks.ui.bottomdrawer.BottomDrawerTasksList
import com.jurajkusnier.googletasks.ui.tasks.TasksFragment
import com.jurajkusnier.googletasks.ui.taskslist.AddListFragment
import com.jurajkusnier.googletasks.ui.taskslist.EditListFragment
import com.jurajkusnier.googletasks.ui.taskslist.TasksListFragment
import com.jurajkusnier.googletasks.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),TasksActivity {

    val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomBar)
    }

    override fun onResume() {
        super.onResume()

        //Setup toolbars for currently loaded fragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val tasksFragment = TasksFragment()
            transaction.replace(R.id.fragment_container, tasksFragment)
            transaction.addToBackStack(null)
            transaction.commit()
            setupToolbarsForFragment(tasksFragment)
        } else {
            setupToolbarsForFragment(currentFragment)
        }
    }

    private fun setupToolbarsForFragment(fragment:Fragment) {
        when (fragment) {
            is TasksListFragment -> {
                hideBottomAppBar()
                showTaskListToolbar(fragment)
            }
            is TasksFragment -> {
                showBottomAppBar()
                hideTaskListToolbar()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.d(TAG,"onOptionsItemSelected($item)")
        return when (item?.itemId) {
            android.R.id.home -> {
                BottomDrawerTasksList().show(supportFragmentManager, BottomDrawerTasksList.TAG)
                true
            }
            R.id.action_more_options -> {
                BottomBarMenu().show(supportFragmentManager, BottomBarMenu.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            finish()
        } else {
            setupToolbarsForFragment(currentFragment)
        }
    }


    override fun showNewTaskListFragment() {
        val addListFragment = AddListFragment()
        showTaskListFragment(addListFragment )
    }

    override fun showEditTaskListFragment(taskList: TaskList) {
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

    private fun hideTaskListToolbar() {
        toolbar.visibility = View.GONE
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
    }

    override fun showUndoSnackbar(taskList: TaskList) {

        val coordinatorView = findViewById<CoordinatorLayout>(R.id.mainCoordinatorLayout)
        if (coordinatorView == null) {
            Log.e(BottomBarMenu.TAG,"Can't show undo snackbar. CoordinatorLayout not found")
            return
        }

        val snackbar = Snackbar.make(coordinatorView,getString(R.string.operation_task_list_deleted), Snackbar.LENGTH_LONG)
        snackbar.setAction(getString(R.string.undo)) {
            viewModel.insertTaskList(taskList)
        }
        val snackBarView = snackbar.view
        val params = snackBarView.layoutParams as CoordinatorLayout.LayoutParams
        params.bottomMargin = resources.getDimension(R.dimen.snackbar_bottom_margin).toInt()

        snackBarView.layoutParams = params
        snackbar.show()
    }

    private val viewModel by lazy {
        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        ViewModelProviders.of(this,viewModelFactory).get(MainActivityViewModel::class.java)
    }


}
