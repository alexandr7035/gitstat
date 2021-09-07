package com.alexandr7035.gitstat.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.data.Repository
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.data.local.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application, user: String, token: String) : AndroidViewModel(application) {

    private val userLiveData = MutableLiveData<UserEntity>()
    private val repositoriesLiveData = MutableLiveData<List<RepositoryEntity>>()
    private val loginResponseCodeLiveData = MutableLiveData<Int>()

    private val repository = Repository(application, user, token)

    // Fixme ID
    fun getUserLiveData(user: String): LiveData<UserEntity> {
        return userLiveData
    }

    fun updateUserData(user: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserLiveDataFromCache(userLiveData, user)
        }
    }

    fun getRepositoriesData(): LiveData<List<RepositoryEntity>> {
        return repositoriesLiveData
    }

    fun updateRepositoriesLiveData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getRepositoriesLiveDataFromCache(repositoriesLiveData)
        }
    }

    fun getSyncStatusLData(): LiveData<SyncStatus> {
        return repository.getSyncStatusLiveData()
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