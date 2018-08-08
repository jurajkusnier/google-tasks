package com.jurajkusnier.googletasks.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.jurajkusnier.googletasks.R
import com.jurajkusnier.googletasks.ui.taskslist.BottomSheetTasksList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomBar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> BottomSheetTasksList().show(supportFragmentManager, BottomSheetTasksList.TAG)
        }

        return super.onOptionsItemSelected(item)
    }
}
