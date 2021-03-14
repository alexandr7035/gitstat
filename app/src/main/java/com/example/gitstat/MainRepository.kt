package com.example.gitstat

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.gitstat.api.GitHubApi
import com.example.gitstat.model.RepositoryModel
import com.example.gitstat.model.UserModel
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository(application: Application, user: String, token: String) {

    private val LOG_TAG = "DEBUG_TAG"

    private lateinit var gitHubApi: GitHubApi
    private lateinit var user: String
    private lateinit var token: String
    private lateinit var authCredentials: String

    init {

        val retrofit = Retrofit.Builder()
                // Fixme - move to strings
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        gitHubApi = retrofit.create(GitHubApi::class.java)

        this.user = user
        this.token = token
        authCredentials = Credentials.basic(this.user, this.token)
    }

    fun getUserData(liveData: MutableLiveData<UserModel>) {
        //liveData.postValue()
        gitHubApi.getUser(authCredentials, user).enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                liveData.value = response.body()
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                //
            }

        })
    }


    fun updateRepositoriesLiveData(liveData: MutableLiveData<List<RepositoryModel>>) {
        gitHubApi.getRepositories(authCredentials, user).enqueue(object : Callback<List<RepositoryModel>> {
            override fun onResponse(call: Call<List<RepositoryModel>>, response: Response<List<RepositoryModel>>) {
                liveData.value = response.body()
            }

            override fun onFailure(call: Call<List<RepositoryModel>>, t: Throwable) {
                //
            }

        })
    }
}