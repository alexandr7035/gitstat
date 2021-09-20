package com.alexandr7035.gitstat.view

import android.app.Application
import androidx.lifecycle.*
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.data.Repository
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.data.local.model.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val userLiveData = MutableLiveData<UserEntity>()
    private val repositoriesLiveData = MutableLiveData<List<RepositoryEntity>>()
    private val loginResponseCodeLiveData = MutableLiveData<Int>()

    //private val repository = Repository(application, token)

    // Fixme ID
    fun getUserLiveData(): LiveData<UserEntity> {
        return userLiveData
    }

    fun updateUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserLiveDataFromCache(userLiveData)
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

    fun doLoginRequest(token: String) {
        repository.doLoginRequest(loginResponseCodeLiveData, token)
    }

    fun getLoginResponseCodeLiveData(): MutableLiveData<Int> {
        return loginResponseCodeLiveData
    }

    fun doLogOut() {
        repository.doLogout()
        repository.clearAllCache()
    }

}