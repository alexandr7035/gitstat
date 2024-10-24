package by.alexandr7035.gitstat.view.datasync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.gitstat.core.DataSyncStatus
import by.alexandr7035.gitstat.data.DataSyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(private val repository: DataSyncRepository): ViewModel() {
    private val syncStatusLiveData = MutableLiveData<DataSyncStatus>()

    fun getSyncStatusLiveData(): LiveData<DataSyncStatus> {
        return syncStatusLiveData
    }

    fun syncData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.syncData(syncStatusLiveData)
        }
    }
}