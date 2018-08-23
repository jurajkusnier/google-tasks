package com.jurajkusnier.googletasks.ui.bottomdrawer

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.data.TaskList
import com.jurajkusnier.googletasks.ui.activity.TasksActivity
import com.jurajkusnier.googletasks.ui.getScreenHeight
import com.jurajkusnier.googletasks.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.navigation_view_tasks_lists.*
import kotlin.math.roundToInt

class BottomDrawerTasksList: AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Get resources
        val staturBarIdentifier = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = if (staturBarIdentifier != 0) resources.getDimensionPixelSize(staturBarIdentifier) else 0
        val cornerRadiusStart = resources.getDimension(R.dimen.bottom_sheet_corner_radius)
        val bottomSheetImageMinimalHeight = resources.getDimension(R.dimen.bottom_sheet_decoration_minimal_height)
        val headerElevation = resources.getDimension(R.dimen.header_elevation)
        val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)

        //Create dialog
        val dialog = Dialog(activity,R.style.BottomSheetTasksListStyle)
        dialog.setContentView(R.layout.navigation_view_tasks_lists)

        //Extends dialog on whole screen (also under status bar)
        dialog.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        var tasksLists:List<TaskList>? = null

        //Observe Tasks List from DB and add it to NavigationView
        mViewModel.taskList.observe(this, Observer {

            tasksLists = it

            val menu = dialog.navigationViewTasksLists.menu

            menu.clear()

            val selectedList = mViewModel.selectedTaskList

            for ((index,taskList) in  it.withIndex()) {
                menu.add(0,index+ MENU_LIST_ITEM_FIRST_INDEX,index, taskList.listName).setCheckable(true).isChecked = (taskList.id == selectedList)
            }

            menu.add(1, ID_MENU_NEW, it.size,R.string.create_list).setIcon(R.drawable.ic_add)
            menu.add(2, ID_MENU_FEEDBACK,it.size + 1,R.string.send_feedback).setIcon(R.drawable.ic_feedback)

            dialog.navigationViewTasksLists.invalidate()
        })



        val behaviour = BottomSheetBehavior.from(dialog.bottomSheetLayout)

        //Setup peekHeight to 50% of the screen
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
                drawable.cornerRadii = floatArrayOf(newRadius,newRadius,newRadius,newRadius,0f,0f,0f,0f)
                drawable.setColor(backgroundColor)

                dialog.imageBackground.setImageDrawable(drawable)

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dialog.cancel()
                }
            }
        })

        //Hide bottom sheet before close
        fun hideBottomSheet() {
            behaviour.state = BottomSheetBehavior.STATE_HIDDEN
        }

        dialog.navigationViewTasksLists.setNavigationItemSelectedListener {

            when(it.itemId) {
                ID_MENU_NEW -> {
                    (activity as? TasksActivity)?.showNewTaskListFragment() ?: throw IllegalStateException("$TAG was used in wrong activity!")
                    hideBottomSheet()
                }
                ID_MENU_FEEDBACK -> showNotImplemented()
                else -> {
                    val task = tasksLists?.get(it.itemId - MENU_LIST_ITEM_FIRST_INDEX)
                    if (task != null) {
                        mViewModel.selectedTaskList = task.id
                    }
                    hideBottomSheet()
                }
            }

            true
        }

        //Add elevation to header when scrolling
        dialog.nestedScrollViewTasksLists.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            dialog.appBarTasksLists.elevation = if (scrollY == 0) 0f else headerElevation
        }

        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                hideBottomSheet()
                true
            }
            false
        }

        dialog.coordinatorLayout.setOnClickListener {
            hideBottomSheet()
        }

        dialog.close_button.setOnClickListener {
            hideBottomSheet()
        }

        dialog.privacy_policy.setOnClickListener {
            showNotImplemented()
        }

        dialog.terms_of_service.setOnClickListener {
            showNotImplemented()
        }

        return dialog
    }

    private fun showNotImplemented() {
        Toast.makeText(activity,getString(R.string.not_implemented),Toast.LENGTH_SHORT).show()
    }

    companion object {
        val TAG = BottomDrawerTasksList::class.java.simpleName
        const val ID_MENU_NEW = 0
        const val ID_MENU_FEEDBACK = 1
        const val MENU_LIST_ITEM_FIRST_INDEX = 2
    }

    private val mViewModel: BottomDrawerTasksListViewModel by lazy {
        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        ViewModelProviders.of(this, viewModelFactory).get(BottomDrawerTasksListViewModel::class.java)
    }
}