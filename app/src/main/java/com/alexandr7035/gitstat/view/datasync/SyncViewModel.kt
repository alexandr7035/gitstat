package com.alexandr7035.gitstat.view.datasync

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.core.DataSyncStatus
import com.alexandr7035.gitstat.data.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(private val repository: SyncRepository): ViewModel() {
    private val syncStatusLiveData = MutableLiveData<DataSyncStatus>()

    fun checkIfTokenSaved(): Boolean {
        return repository.checkIfTokenSaved()
    }

    fun clearToken() {
        repository.clearToken()
    }

    fun getSyncStatusLiveData(): MutableLiveData<DataSyncStatus> {
        return syncStatusLiveData
    }

    fun syncData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.syncAllData(syncStatusLiveData)
        }
    }

    fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearCache()
        }
    }
}