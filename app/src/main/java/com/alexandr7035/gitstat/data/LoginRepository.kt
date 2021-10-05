package com.alexandr7035.gitstat.data

import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.data.local.CacheDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor(private val appPreferences: AppPreferences, private val dao: CacheDao) {

    fun doLoginRequest(loginLiveData: MutableLiveData<Int>, token: String) {

        CoroutineScope(Dispatchers.IO).launch {
            try {

                withContext(Dispatchers.Main) {
                    // FIXME temp
                    loginLiveData.value = 200
                }
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loginLiveData.value = 0
                }
            }
        }

    }


    fun saveToken(token: String) {
        appPreferences.token = token
    }

    fun checkIfLoggedIn(): Boolean {
        return when (appPreferences.token) {
            null -> false
            else -> true
        }
    }

    fun clearToken() {
        appPreferences.token = null
    }

    suspend fun clearCache() {
        dao.clearUserCache()
        dao.clearRepositoriesCache()
    }
}