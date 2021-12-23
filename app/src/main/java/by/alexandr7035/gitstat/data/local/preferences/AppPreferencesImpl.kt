package by.alexandr7035.gitstat.data.local.preferences

import by.alexandr7035.gitstat.data.local.KeyValueStorage
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(private val keyValueStorage: KeyValueStorage): AppPreferences {
    override fun getToken(): String? {
        val fromStorage = keyValueStorage.getString(TOKEN_KEY, "null")

        return if (fromStorage == "null") {
            null
        }
        else {
            fromStorage
        }
    }

    override fun saveToken(token: String?) {
        keyValueStorage.putString(TOKEN_KEY, token ?: "null")
    }

    override fun getLastCacheSyncDate(): Long {
        return keyValueStorage.getLong(CACHE_SYNC_DATE_KEY, 0)
    }

    override fun saveLastCacheSyncDate(date: Long) {
        keyValueStorage.putLong(CACHE_SYNC_DATE_KEY, date)
    }

    override fun getRepositoriesFilters(): String? {
        val fromStorage = keyValueStorage.getString(REPOSITORIES_FILTERS_KEY, "null")

        return if (fromStorage == "null") {
            null
        }
        else {
            fromStorage
        }
    }

    override fun saveRepositoriesFilters(filtersStr: String?) {
        keyValueStorage.putString(REPOSITORIES_FILTERS_KEY, filtersStr ?: "null")
    }

    override fun getLastInstalledVersionCode(): Int {
        return keyValueStorage.getInt(LAST_INSTALLED_VERSION_CODE_KEY, 0)
    }

    override fun saveLastInstalledVersionCode(versionCode: Int) {
        keyValueStorage.putInt(LAST_INSTALLED_VERSION_CODE_KEY, versionCode)
    }

    companion object {
        private const val TOKEN_KEY = "TOKEN"
        private const val CACHE_SYNC_DATE_KEY = "LAST_CACHE_SYNC_DATE"
        private const val REPOSITORIES_FILTERS_KEY = "REPOSITORIES_FILTERS"
        private const val LAST_INSTALLED_VERSION_CODE_KEY = "LAST_INSTALLED_VERSION_CODE"
    }
}