package com.alexandr7035.gitstat.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.common.SyncStatus
import com.alexandr7035.gitstat.data.Repository
import com.alexandr7035.gitstat.data.local.RepositoryEntity
import com.alexandr7035.gitstat.data.local.UserEntity

class MainViewModel(application: Application, user: String, token: String) : AndroidViewModel(application) {

    private val loginResponseCodeLiveData = MutableLiveData<Int>()

    private val repository = Repository(application, user, token)

    // Fixme ID
    fun getUserLData(user: String): LiveData<UserEntity> {
        return repository.getUserLiveDataFromCache(user)
    }

    fun getSyncStatusLData(): LiveData<SyncStatus> {
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