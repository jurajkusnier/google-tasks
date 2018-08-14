package com.jurajkusnier.googletasks.ui.taskslist

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.SharedPreferencesHelper
import com.jurajkusnier.googletasks.db.TaskList
import com.jurajkusnier.googletasks.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.navigation_view_tasks_lists.*
import kotlin.math.roundToInt


fun Activity.getScreenHeight():Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

class BottomSheetTasksList: AppCompatDialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val staturBarIdentifier = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = if (staturBarIdentifier != 0) resources.getDimensionPixelSize(staturBarIdentifier) else 0
        val cornerRadiusStart = resources.getDimension(R.dimen.bottom_sheet_corner_radius)
        val bottomSheetImageMinimalHeight = resources.getDimension(R.dimen.bottom_sheet_decoration_minimal_height)

        val dialog = Dialog(activity,R.style.BottomSheetTasksListStyle)

        dialog.setContentView(R.layout.navigation_view_tasks_lists)

        dialog.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) //Allows dialog under status bar

        val navigationView = dialog.navigationViewTasksLists
        val nestedStrollView = dialog.nestedScrollViewTasksLists

        nestedStrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.appBarTasksLists.elevation = if (scrollY == 0) {
                    0f
                } else {
                    15f
                }
            }
        }

        viewModel.taskList.observe(this, Observer {

            tasksList = it

            val menu = navigationView.menu

            menu.clear()

            val selectedList = preferencesHelper.selectedTaskList

            for ((index,taskList) in  it.withIndex()) {
                menu.add(0,index+2,index, taskList.listName).setCheckable(true).isChecked = (taskList.id == selectedList || index == 0)
            }

            menu.add(1,0,it.size,R.string.create_list).setIcon(R.drawable.ic_add)
            menu.add(2,1,it.size + 1,R.string.send_feedback).setIcon(R.drawable.ic_feedback)

            navigationView.invalidate()
        })

        navigationView.setNavigationItemSelectedListener {

            when(it.itemId) {
                0 -> viewModel.insertTaskList(TaskList("My Task List (${System.currentTimeMillis() % 1000})"))
                1 -> {}
//                 TODO: selection is changed for delete, only for testing
//                else -> preferencesHelper.selectedTaskList =  tasksList?.get(it.itemId - 2)?.id ?: 0
                else -> {
                    val task = tasksList?.get(it.itemId -2)
                    if (task != null) {
                        viewModel.deleteTaskList(task)
                    }
                }
            }

            true
        }

        val behaviour = BottomSheetBehavior.from(dialog.bottomSheetLayout)

        val screenHeight = activity?.getScreenHeight()
        if (screenHeight != null) behaviour.peekHeight = (screenHeight * 0.5f).roundToInt() + statusBarHeight

        dialog.imageBackground.setPadding(0, statusBarHeight,0,0)
        val lp = dialog.imageBackground.layoutParams
        lp.height = (bottomSheetImageMinimalHeight + statusBarHeight).roundToInt()
        dialog.imageBackground.layoutParams = lp

        behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(view: View, p1: Float) {

                val interpolator = Math.max(p1 - 0.75f,0f) * 4f

                if (dialog.coordinatorLayout.height <= dialog.bottomSheetLayout.height) {
                    dialog.close_button.alpha = interpolator
                    val paddingTop = (1f - interpolator) * statusBarHeight
                    dialog.imageBackground.setPadding(0, paddingTop.roundToInt(), 0, 0)
                }

                val drawable = GradientDrawable()
                val newRadius = (1f - interpolator) * cornerRadiusStart
                val f = floatArrayOf(newRadius,newRadius,newRadius,newRadius,0f,0f,0f,0f)
                drawable.cornerRadii = f
                drawable.setColor(Color.WHITE)

                dialog.imageBackground.setImageDrawable(drawable)

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dialog.cancel()
                }
            }
        })

        return dialog
    }

    companion object {
        val TAG = BottomSheetTasksList::class.java.simpleName
    }

    private val preferencesHelper: SharedPreferencesHelper by lazy {
        SharedPreferencesHelper(context!!)
    }

    private val viewModel:BottomSheetTasksListViewModel by lazy {
        val viewModelFactory = ViewModelFactory.getInstance(context?.applicationContext as Application)
        ViewModelProviders.of(this, viewModelFactory).get(BottomSheetTasksListViewModel::class.java)
    }

    var tasksList:List<TaskList>? = null

}