package com.alexandr7035.gitstat.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App(): Application() {
    override fun onCreate() {
        super.onCreate()
    }
}