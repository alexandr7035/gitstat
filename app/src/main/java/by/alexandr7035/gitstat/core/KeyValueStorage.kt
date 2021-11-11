package by.alexandr7035.gitstat.core

interface KeyValueStorage {
    fun getToken(): String?

    fun saveToken(token: String?)

    fun getLastCacheSyncDate(): Long

    fun saveLastCacheSyncDate(date: Long)

    fun getRepositoriesFilters(): String?

    fun saveRepositoriesFilters(filtersStr: String?)
}