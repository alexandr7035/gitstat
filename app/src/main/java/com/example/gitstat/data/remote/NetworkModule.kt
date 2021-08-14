package com.example.gitstat.data.remote

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
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


    fun updateRepositoriesLiveData(liveData: MutableLiveData<List<RepositoryModel>>) {
        // FIXME fix if user has more than 100 repos
        gitHubApi.getRepositories(authCredentials, userParam="user:$user", page=1, perPage=100).enqueue(object : Callback<ReposSearchModel> {
            override fun onResponse(call: Call<ReposSearchModel>, response: Response<ReposSearchModel>) {

                val searchResults = response.body()
                Log.d(LOG_TAG, "url ${call.request().url()}")
                liveData.value = searchResults?.items

                Log.d(LOG_TAG, "REPOS LIST ${response.body()?.items?.size}")
            }

            override fun onFailure(call: Call<ReposSearchModel>, t: Throwable) {
                val message: String? = t.message
                Log.d(LOG_TAG,  "FAILURE $message")
                //syncStateLiveData.postValue("$message")
            }

        })
    }

}