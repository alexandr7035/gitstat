package com.alexandr7035.gitstat.core

import android.app.Application
import com.alexandr7035.gitstat.data.remote.ApolloHelperImpl
import com.apollographql.apollo3.ApolloClient
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App(): Application() {

    lateinit var progLangManager: ProgLangManager

    override fun onCreate() {
        super.onCreate()
        progLangManager = ProgLangManager(context = applicationContext)
    }
}