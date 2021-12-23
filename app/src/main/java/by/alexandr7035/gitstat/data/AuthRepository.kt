package by.alexandr7035.gitstat.data

import by.alexandr7035.gitstat.data.local.preferences.AppPreferences
import javax.inject.Inject

class AuthRepository @Inject constructor(private val appPreferences: AppPreferences) {

    fun saveToken(token: String) {
        appPreferences.saveToken(token)
    }

}