package com.vladtruta.kitchenapp.utils

import android.app.Application

class KitchenApp: Application() {
    companion object {
        lateinit var instance: KitchenApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}