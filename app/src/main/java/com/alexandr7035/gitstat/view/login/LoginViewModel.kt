package com.alexandr7035.gitstat.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.data.LoginRepository
import com.alexandr7035.gitstat.data.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val syncRepository: SyncRepository): ViewModel() {

    private val resCodeLiveData = MutableLiveData<Int>()
    private val syncStatusLiveData = MutableLiveData<SyncStatus>()

    fun doLoginRequest(token: String) {
        loginRepository.doLoginRequest(resCodeLiveData, token)
    }

    fun getLoginResponseCodeLiveData(): LiveData<Int> {
        return resCodeLiveData
    }

    fun saveToken(token: String) {
        loginRepository.saveToken(token)
    }

    fun checkIfLoggedIn(): Boolean {
        return loginRepository.checkIfLoggedIn()
    }

    fun logOut() {
        loginRepository.clearToken()

        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.clearCache()
        }
    }


    fun getSyncStatusLiveData(): MutableLiveData<SyncStatus> {
        return syncStatusLiveData
    }

    fun syncData() {
        // FIXME sync all data here
        viewModelScope.launch(Dispatchers.IO) {
            syncRepository.syncAllContributions(syncStatusLiveData)
        }
    }

}