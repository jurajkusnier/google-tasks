package com.jurajkusnier.googletasks

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {

    companion object {
        private const val prefSelectedTaskListId = "SELECTED_TASK_LIST_ID"
    }

    private val prefManager = PreferenceManager.getDefaultSharedPreferences(context)

    var selectedTaskList:Int
        get() = prefManager.getInt(prefSelectedTaskListId,0)
        set(value) { prefManager.edit().putInt(prefSelectedTaskListId,value).apply() }
}