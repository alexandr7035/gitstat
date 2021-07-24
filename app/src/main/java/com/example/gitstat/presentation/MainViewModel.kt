package com.example.gitstat.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.gitstat.data.MainRepository
import com.example.gitstat.data.model.RepositoryModel
import com.example.gitstat.data.model.UserModel

class MainViewModel(application: Application, user: String, token: String) : AndroidViewModel(application) {
    val userLiveData = MutableLiveData<UserModel>()
    val reposLiveData = MutableLiveData<List<RepositoryModel>>()
    val emailLiveData = MutableLiveData<String>()

    private val repository = MainRepository(application, user, token)

    var msgLiveData = repository.getMessageLiveData()

    fun updateUserData() {
        repository.getUserData(userLiveData)
    }

    fun updateRepositoriesData() {
        repository.updateRepositoriesLiveData(reposLiveData)
    }

}