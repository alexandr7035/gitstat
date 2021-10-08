package com.alexandr7035.gitstat.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.core.DataSyncStatus
import com.alexandr7035.gitstat.data.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val syncRepository: SyncRepository): ViewModel() {

    private val syncStatusLiveData = MutableLiveData<DataSyncStatus>()

    fun checkIfTokenSaved(): Boolean {
        return syncRepository.checkIfTokenSaved()
    }

    fun getSyncStatusLiveData(): MutableLiveData<DataSyncStatus> {
        return syncStatusLiveData
    }

    fun syncData() {
        viewModelScope.launch(Dispatchers.IO) {
            syncRepository.syncAllData(syncStatusLiveData)
        }
    }
}