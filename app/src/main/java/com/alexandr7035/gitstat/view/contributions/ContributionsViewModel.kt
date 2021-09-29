package com.alexandr7035.gitstat.view.contributions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.data.ContributionsRepository
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContributionsViewModel @Inject constructor(private val repository: ContributionsRepository): ViewModel() {
    fun test() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.test()
        }
    }

    fun syncContributions() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.syncContributions()
        }
    }


    // FIXME by year
    fun getContributions(): LiveData<List<ContributionDayEntity>> {
        return repository.getContributions()
    }
}