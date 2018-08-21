package com.jurajkusnier.googletasks.ui.taskslist

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.jurajkusnier.googletasks.App
import com.jurajkusnier.googletasks.OrderBy
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.SharedPreferencesHelper
import com.jurajkusnier.googletasks.db.AppDatabase.Companion.FIRST_ITEM_ID
import com.jurajkusnier.googletasks.db.TaskList
import com.jurajkusnier.googletasks.di.Injection
import com.jurajkusnier.googletasks.ui.MainActivity
import com.jurajkusnier.googletasks.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.bottom_sheet_menu.*

class BottomBarMenu: BottomSheetDialogFragment() {

    companion object {
        val TAG = BottomBarMenu::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_menu,container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.BottomSheetModalDialog)
    }

    private val viewModel by lazy {
        val viewModelFactory = ViewModelFactory.getInstance(App.applicationContext as Application)
        ViewModelProviders.of(this,viewModelFactory).get(BottomBarMenuViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        val currentActivity = activity as? MainActivity ?: throw IllegalStateException("$TAG was used in wrong activity!")

        dialog.apply {
            delete_all_completed_tasks_option.isEnabled = false
            rename_list_option.isEnabled = false
            delete_list_option.isEnabled = false
        }

        viewModel.currentTaskList.observe(this, Observer{ taskList ->

            dialog.apply {
                rename_list_option.isEnabled = true

                rename_list_option.setOnClickListener {
                    currentActivity.showEditTaskListFragment(taskList)
                    dismiss()
                }

                if (taskList.id != FIRST_ITEM_ID) {
                    delete_list_option.isEnabled = true
                    delete_list_option.setOnClickListener {
                        dismiss()
                        preferencesHelper.selectedTaskList = FIRST_ITEM_ID
                        viewModel.tasksListRepository.deleteTaskList(taskList)
                        showUndoSnackbar(taskList)
                    }
                }
            }
        })


        dialog.my_order_option.setOnClickListener {
            preferencesHelper.listOrder = OrderBy.MY_ORDER
            refreshCheckOptionItems()
            dismiss()
        }

        dialog.order_by_due_date_option.setOnClickListener {
            preferencesHelper.listOrder = OrderBy.DATE
            refreshCheckOptionItems()
            dismiss()
        }

        refreshCheckOptionItems()
    }

    private fun showUndoSnackbar(taskList: TaskList) {

        val coordinatorView = activity?.findViewById<CoordinatorLayout>(R.id.mainCoordinatorLayout)
        if (coordinatorView == null) {
            Log.e(TAG, "Can't show undo snackbar. CoordinatorLayout not found")
            return
        }

        val snackbar = Snackbar.make(coordinatorView,getString(R.string.operation_task_list_deleted), Snackbar.LENGTH_LONG)
        snackbar.setAction(getString(R.string.undo)) {
            Injection.provideTaskListRespository(App.applicationContext).insertTaskList(taskList)
            preferencesHelper.selectedTaskList = taskList.id
        }
        val snackBarView = snackbar.view
        val params = snackBarView.layoutParams as CoordinatorLayout.LayoutParams
        params.bottomMargin = resources.getDimension(R.dimen.snackbar_bottom_margin).toInt()

        snackBarView.layoutParams = params
        snackbar.show()
    }

    private fun refreshCheckOptionItems() {
        val orderBy = preferencesHelper.listOrder
        dialog.my_order_option.isChecked = orderBy == OrderBy.MY_ORDER
        dialog.order_by_due_date_option.isChecked = orderBy == OrderBy.DATE

    }

    private val preferencesHelper = SharedPreferencesHelper.getInstance(App.applicationContext)

}