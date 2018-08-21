package com.jurajkusnier.googletasks

import android.app.Application
import android.content.Context

class App: Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance:App

        val applicationContext: Context
            get() = instance.applicationContext
    }

}