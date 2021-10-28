package by.alexandr7035.gitstat.core

import android.app.Application
import android.content.Context
import by.alexandr7035.gitstat.R
import javax.inject.Inject

class AppPreferences @Inject constructor(private val application: Application) {
    private val PREFS_NAME = application.getString(R.string.app_name)
    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var token: String?
        get() = prefs.getString(application.getString(R.string.shared_pref_token), null)
        set(value) = prefs.edit().putString(application.getString(R.string.shared_pref_token), value).apply()

    var repositoriesFilters: String?
        get() = prefs.getString(application.getString(R.string.shared_prefs_filters), null)
        set(value) = prefs.edit().putString(application.getString(R.string.shared_prefs_filters), value).apply()

    var lastSuccessCacheSyncDate: Long
        get() = prefs.getLong(application.getString(R.string.shared_pref_cache_sync_date), 0)
        set(value) = prefs.edit().putLong(application.getString(R.string.shared_pref_cache_sync_date), value).apply()
}