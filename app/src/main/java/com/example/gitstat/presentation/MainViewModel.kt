package com.example.gitstat.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gitstat.data.Repository
import com.example.gitstat.data.local.RepositoryEntity
import com.example.gitstat.data.local.UserEntity
import com.example.gitstat.data.remote.UserModel
import retrofit2.Response

class MainViewModel(application: Application, user: String, token: String) : AndroidViewModel(application) {

    private val loginResponseCodeLiveData = MutableLiveData<Int>()

    private val repository = Repository(application, user, token)

    // Fixme ID
    fun getUserLData(): LiveData<UserEntity> {
        return repository.getUserLiveDataFromCache(user_id = 22574399)
    }

    fun getSyncStatusLData(): LiveData<String> {
        return repository.getSyncStatusLiveData()
    }

    fun getRepositoriesData(): LiveData<List<RepositoryEntity>> {
        return repository.getRepositoriesLiveDataFromCache()
    }


    fun doLoginRequest(user: String, token: String) {
        repository.doLoginRequest(loginResponseCodeLiveData, user, token)
    }

    fun getLoginResponseCodeLiveData(): MutableLiveData<Int> {
        return loginResponseCodeLiveData
    }

    fun clearCache() {
        repository.clearAllCache()
    }

}