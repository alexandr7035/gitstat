package com.alexandr7035.gitstat.data.remote

import com.alexandr7035.gitstat.data.remote.model.RepositoryModel
import com.alexandr7035.gitstat.data.remote.model.UserModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkModule(token: String) {

    private val LOG_TAG = "DEBUG_TAG"

    private var gitHubApi: GitHubApi
    private var token: String
    private var auth: String

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .retryOnConnectionFailure(false)
            .build()

        val retrofit = Retrofit.Builder()
                // Fixme - move to strings
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        gitHubApi = retrofit.create(GitHubApi::class.java)

        this.token = token
        auth = "token ${this.token}"

    }

    suspend fun getUserData(): Response<UserModel> {
        return gitHubApi.getUser(auth)
    }

    suspend fun getRepositoriesData(): Response<List<RepositoryModel>> {
        return gitHubApi.getRepositories(auth)
    }

    // FIXME temp
    suspend fun loginRequest(token: String): Response<UserModel> {
        val auth = "token $token"
        return gitHubApi.getUser(auth)
    }

}