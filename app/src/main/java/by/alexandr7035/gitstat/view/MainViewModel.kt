package by.alexandr7035.gitstat.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.gitstat.data.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val syncRepository: SyncRepository): ViewModel() {

    fun checkIfTokenSaved(): Boolean {
        return syncRepository.checkIfTokenSaved()
    }

    fun clearToken() {
        syncRepository.clearToken()
    }

    fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            syncRepository.clearCache()
        }
    }
}