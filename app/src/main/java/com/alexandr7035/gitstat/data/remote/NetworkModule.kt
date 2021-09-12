package com.alexandr7035.gitstat.data.remote

import android.app.Application
import com.alexandr7035.gitstat.data.remote.model.ReposSearchModel
import com.alexandr7035.gitstat.data.remote.model.UserModel
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkModule(application: Application, user: String, token: String) {

    private val LOG_TAG = "DEBUG_TAG"

    private var gitHubApi: GitHubApi
    private var user: String
    private var token: String
    private var authCredentials: String


    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()

        val retrofit = Retrofit.Builder()
                // Fixme - move to strings
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        gitHubApi = retrofit.create(GitHubApi::class.java)

        this.user = user
        this.token = token
        authCredentials = "token ${this.token}"

    }


    suspend fun getUserData(): Response<UserModel> {
        return gitHubApi.getUser(authCredentials, user)
    }

    suspend fun getRepositoriesData(): Response<ReposSearchModel> {
        return gitHubApi.getRepositories(authCredentials, userParam = "user:$user", page = 1, perPage = 100)
    }


    // FIXME temp
    suspend fun loginRequest(user: String, token: String): Response<UserModel> {
        val creds = "token $token"
        return gitHubApi.getUser(creds, user)
    }

}