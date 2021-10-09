package com.alexandr7035.gitstat.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.apollo.ProfileCreationDateQuery
import com.alexandr7035.gitstat.core.AppError
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.core.AuthStatus
import com.alexandr7035.gitstat.core.ErrorType
import com.apollographql.apollo3.ApolloClient
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val appPreferences: AppPreferences) {

    suspend fun authorize(authResultLiveData: MutableLiveData<AuthStatus>) {

        Log.d("DEBUG_TAG", "auth request")

        try {

            val response = apolloClient.query(ProfileCreationDateQuery())

            if (response.hasErrors()) {
                authResultLiveData.postValue(AuthStatus.UNKNOWN_ERROR)
            } else {
                if (response.data == null) {
                    authResultLiveData.postValue(AuthStatus.UNKNOWN_ERROR)
                } else {
                    authResultLiveData.postValue(AuthStatus.SUCCESS)
                }
            }
        }
        catch (e: AppError) {
            Log.d("DEBUG_TAG", "handle AppError")
            when (e.type) {
                ErrorType.FAILED_AUTHORIZATION -> authResultLiveData.postValue(AuthStatus.FAILED_CREDENTIALS)
                ErrorType.FAILED_CONNECTION -> authResultLiveData.postValue(AuthStatus.FAILED_NETWORK)
                else -> authResultLiveData.postValue(AuthStatus.UNKNOWN_ERROR)
            }
        }

    }

    fun saveToken(token: String) {
        appPreferences.token = token
    }

    fun clearToken() {
        appPreferences.token = null
    }

}