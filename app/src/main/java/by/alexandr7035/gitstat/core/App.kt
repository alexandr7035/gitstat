package by.alexandr7035.gitstat.core

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import by.alexandr7035.gitstat.BuildConfig
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.extensions.debug
import by.alexandr7035.gitstat.data.local.KeyValueStorageImpl
import by.alexandr7035.gitstat.data.local.preferences.AppPreferencesImpl
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.fg_notification_channel)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(getString(R.string.NOTIFICATION_CHANNEL_ID), name, importance)
            channel.setSound(null, null)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        // Force cache (re)synchronization on start if new version was installed
        // Cache DB schema may be changed and some fields may become 0
        val appPrefs = AppPreferencesImpl(KeyValueStorageImpl(this))
        if (appPrefs.getLastInstalledVersionCode() != BuildConfig.VERSION_CODE) {
            appPrefs.saveLastInstalledVersionCode(BuildConfig.VERSION_CODE)
            appPrefs.saveLastCacheSyncDate(0)
        }
    }
}