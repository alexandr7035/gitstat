package by.alexandr7035.gitstat.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.gitstat.data.DataSyncRepository
import by.alexandr7035.gitstat.data.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val syncRepository: SyncRepository, private val dataSyncRepository: DataSyncRepository): ViewModel() {

    fun checkIfTokenSaved(): Boolean {
        return syncRepository.checkIfTokenSaved()
    }

    fun checkIfCacheExists(): Boolean {
        return dataSyncRepository.checkIfCacheExists()
    }

    fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            syncRepository.clearCache()
        }
    }

    fun getCacheSyncDate(): String {
        return syncRepository.getLastCacheSyncDateText()
    }

    fun clearToken() {
        syncRepository.clearToken()
    }
}