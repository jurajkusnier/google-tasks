package com.jurajkusnier.googletasks.ui.bottombarmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.data.AppDatabase.Companion.FIRST_ITEM_ID
import com.jurajkusnier.googletasks.data.OrderBy
import com.jurajkusnier.googletasks.ui.activity.TasksActivity
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
        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        ViewModelProviders.of(this,viewModelFactory).get(BottomBarMenuViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        val currentActivity = activity as? TasksActivity
                ?: throw IllegalStateException("$TAG was used in wrong activity!")

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
                        viewModel.deleteTaskList(taskList)
                        (activity as? TasksActivity)?.showUndoSnackbar(taskList) ?: throw IllegalStateException("$TAG was used in wrong activity!")
                    }
                }
            }
        })


        dialog.my_order_option.setOnClickListener {
            viewModel.listOrder = OrderBy.MY_ORDER
            refreshCheckOptionItems()
            dismiss()
        }

        dialog.order_by_due_date_option.setOnClickListener {
            viewModel.listOrder = OrderBy.DATE
            refreshCheckOptionItems()
            dismiss()
        }

        refreshCheckOptionItems()
    }

    private fun refreshCheckOptionItems() {
        val orderBy = viewModel.listOrder
        dialog.my_order_option.isChecked = orderBy == OrderBy.MY_ORDER
        dialog.order_by_due_date_option.isChecked = orderBy == OrderBy.DATE

    }

}