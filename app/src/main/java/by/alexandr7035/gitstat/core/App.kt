package by.alexandr7035.gitstat.core

import android.app.Application
import by.alexandr7035.gitstat.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App(): Application() {
    override fun onCreate() {
        super.onCreate()

        if (by.alexandr7035.gitstat.BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}