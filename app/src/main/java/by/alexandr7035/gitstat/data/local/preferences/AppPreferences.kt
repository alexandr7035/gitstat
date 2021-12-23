package by.alexandr7035.gitstat.data.local.preferences

interface AppPreferences {
    fun getToken(): String?

    fun saveToken(token: String?)

    fun getLastCacheSyncDate(): Long

    fun saveLastCacheSyncDate(date: Long)

    fun getRepositoriesFilters(): String?

    fun saveRepositoriesFilters(filtersStr: String?)

    fun getLastInstalledVersionCode(): Int

    fun saveLastInstalledVersionCode(versionCode: Int)
}