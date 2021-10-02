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


    fun syncLastYearContributions() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.syncLastYearContributions()
        }
    }


    // FIXME by year
    fun getLastYearContributions(): LiveData<List<ContributionDayEntity>> {
        return repository.getLastYearContributions()
    }
}