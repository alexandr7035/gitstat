package com.alexandr7035.gitstat.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.data.UserRepository
import com.alexandr7035.gitstat.data.local.model.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    fun getUserLiveData(): LiveData<UserEntity> {
        return userRepository.getUserLiveDataFromCache()
    }

    fun getSyncStatusLiveData(): LiveData<SyncStatus> {
        return userRepository.getSyncStatusLiveData()
    }

    fun syncUserDta() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.syncUserData()
        }
    }

}