package com.alexandr7035.gitstat.data.remote

import android.app.Application
import android.util.Log
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.data.remote.model.RepositoryModel
import com.alexandr7035.gitstat.data.remote.model.UserModel
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class NetworkModule @Inject constructor(private val appPrefs: AppPreferences, private val gitHubApi: GitHubApi) {

    private val LOG_TAG = "DEBUG_TAG"

    suspend fun getUserData(): Response<UserModel> {
        Log.d("DEBUG_TAG", appPrefs.hashCode().toString())

        return gitHubApi.getUser("token ${appPrefs.token}")
    }

    suspend fun getRepositoriesData(): Response<List<RepositoryModel>> {
        return gitHubApi.getRepositories("token ${appPrefs.token}")
    }


    suspend fun loginRequest(token: String): Response<UserModel> {
        return gitHubApi.getUser("token ${token}")
    }

}