package com.alexandr7035.gitstat.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App(): Application() {

    lateinit var progLangManager: ProgLangManager

    override fun onCreate() {
        super.onCreate()
        progLangManager = ProgLangManager(context = applicationContext)
    }
}