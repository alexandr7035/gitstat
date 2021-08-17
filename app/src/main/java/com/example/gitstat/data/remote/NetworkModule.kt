package com.example.gitstat.data.remote

import android.app.Application
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkModule(application: Application, user: String, token: String) {

    private val LOG_TAG = "DEBUG_TAG"

    private lateinit var gitHubApi: GitHubApi
    private lateinit var user: String
    private lateinit var token: String
    private lateinit var authCredentials: String


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
        authCredentials = Credentials.basic(this.user, this.token)

    }


    suspend fun getUserData(): Response<UserModel> {
        return gitHubApi.getUser(authCredentials, user)
    }

    suspend fun getRepositoriesData(): Response<ReposSearchModel> {
        return gitHubApi.getRepositories(authCredentials, userParam = "user:$user", page = 1, perPage = 100)
    }

}