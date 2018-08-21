package com.jurajkusnier.googletasks

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesHelper private constructor (context: Context) {

    companion object {
        private val TAG = SharedPreferencesHelper::class.java.simpleName
        private const val prefSelectedTaskListId = "SELECTED_TASK_LIST_ID"
        private const val prefOrderById = "ORDER_BY"

        @Volatile private var instance: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            return instance ?: synchronized(this) {
                instance = SharedPreferencesHelper(context)
                instance ?: throw IllegalAccessException("Can't instantiate class ${SharedPreferencesHelper.TAG}")
            }
        }
    }

    private val prefManager = PreferenceManager.getDefaultSharedPreferences(context)

    var selectedTaskList: Int
        get() = prefManager.getInt(prefSelectedTaskListId, 0)
        set(value) {
            prefManager.edit().putInt(prefSelectedTaskListId, value).apply()
        }

    var listOrder: OrderBy
        get() = OrderBy.fromInt(prefManager.getInt(prefOrderById,OrderBy.MY_ORDER.value))
        set(value) {
            prefManager.edit().putInt(prefOrderById,value.value).apply()
        }

}

enum class OrderBy(val value:Int) {
    MY_ORDER(0),
    DATE(1);

    companion object {
        fun fromInt(i:Int):OrderBy = if (i == 1) DATE else MY_ORDER
    }
}