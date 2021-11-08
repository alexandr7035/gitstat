package by.alexandr7035.gitstat.data

import android.app.Application
import android.content.Context
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.KeyValueStorage
import javax.inject.Inject

class AppPreferences @Inject constructor(private val application: Application): KeyValueStorage {

    private val PREFS_NAME = application.getString(R.string.app_name)
    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getToken(): String? {
        return prefs.getString(application.getString(R.string.shared_pref_token), null)
    }

    override fun saveToken(token: String?) {
        prefs.edit().putString(application.getString(R.string.shared_pref_token), token).apply()
    }

    override fun getLastCacheSyncDate(): Long {
        return prefs.getLong(application.getString(R.string.shared_pref_cache_sync_date), 0)
    }

    override fun saveLastCacheSyncDate(date: Long) {
        prefs.edit().putLong(application.getString(R.string.shared_pref_cache_sync_date), date).apply()
    }

    override fun getRepositoriesFilters(): String? {
        return prefs.getString(application.getString(R.string.shared_prefs_filters), null)
    }

    override fun saveRepositoriesFilters(filtersStr: String?) {
        prefs.edit().putString(application.getString(R.string.shared_prefs_filters), filtersStr).apply()
    }
}