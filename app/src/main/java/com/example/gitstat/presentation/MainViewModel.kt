package com.example.gitstat.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.gitstat.data.Repository
import com.example.gitstat.data.remote.NetworkModule
import com.example.gitstat.data.remote.RepositoryModel
import com.example.gitstat.data.remote.UserModel

class MainViewModel(application: Application, user: String, token: String) : AndroidViewModel(application) {

    private val repository = Repository(application, user, token)

    // Fixme ID
    val userLiveData = repository.getUserLiveDataFromCache(user_id = 22574399)
    val languagesLiveData = repository.getLanguagesLiveDataFromCache()
    val syncStatusLiveData = repository.getSyncStatusLiveData()

}