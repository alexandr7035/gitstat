package by.alexandr7035.gitstat.data.local

interface KeyValueStorage {
    fun getString(key: String, defaultValue: String): String

    fun putString(key: String, value: String)

    fun getLong(key: String, defaultValue: Long): Long

    fun putLong(key: String, value: Long)

    fun getInt(key: String, defaultValue: Int): Int

    fun putInt(key: String, value: Int)
}