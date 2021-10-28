package by.alexandr7035.gitstat.data

import by.alexandr7035.gitstat.core.AppPreferences
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val appPreferences: AppPreferences
) {

    fun saveToken(token: String) {
        appPreferences.token = token
    }

    fun clearToken() {
        Timber.tag("DEBUG_AUTH").d("auth repo clear token")
        appPreferences.token = null
    }

}