package com.example.gitstat

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.gitstat.api.GitHubApi
import com.example.gitstat.model.*
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

            }

        })
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
            }

        })
    }


    fun updateEmailLiveData(liveData: MutableLiveData<String>) {
        gitHubApi.getUserEmails(authCredentials).enqueue(object: Callback<List<EmailModel>> {
            override fun onResponse(
                call: Call<List<EmailModel>>,
                response: Response<List<EmailModel>>) {

                if (response.body() != null) {
                    response.body()!!.forEach {
                        if (it.primary) {
                            // Post email string
                            liveData.postValue(it.email)
                        }
                    }
                }

            }

            override fun onFailure(call: Call<List<EmailModel>>, t: Throwable) {
                val message: String? = t.message
                Log.d(LOG_TAG,  "FAILURE $message")
            }
        })
    }
}