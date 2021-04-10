package com.example.gitstat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.gitstat.model.RepositoryModel
import com.example.gitstat.model.UserModel

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

    fun updateEmailData() {
        repository.updateEmailLiveData(emailLiveData)
    }

}