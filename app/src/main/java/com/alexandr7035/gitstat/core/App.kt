package com.alexandr7035.gitstat.core

import android.app.Application

class App: Application() {

    lateinit var progLangManager: ProgLangManager

    override fun onCreate() {
        super.onCreate()
        progLangManager = ProgLangManager(context = applicationContext)
    }
}