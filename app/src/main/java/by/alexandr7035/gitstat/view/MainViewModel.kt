package by.alexandr7035.gitstat.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.gitstat.data.DataSyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataSyncRepository: DataSyncRepository): ViewModel() {

    fun checkIfTokenSaved(): Boolean {
        return dataSyncRepository.checkIfTokenSaved()
    }

    fun checkIfCacheExists(): Boolean {
        return dataSyncRepository.checkIfCacheExists()
    }

    fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            dataSyncRepository.clearCache()
        }
    }

    fun getCacheSyncDate(): String {
        return dataSyncRepository.getLastCacheSyncDateText()
    }

    fun clearToken() {
        dataSyncRepository.clearToken()
    }
}