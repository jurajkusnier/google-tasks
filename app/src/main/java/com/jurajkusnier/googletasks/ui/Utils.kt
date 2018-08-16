package com.jurajkusnier.googletasks.ui

import android.app.Activity
import android.util.DisplayMetrics

fun Activity.getScreenHeight():Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}