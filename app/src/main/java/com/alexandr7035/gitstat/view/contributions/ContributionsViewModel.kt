package com.alexandr7035.gitstat.view.contributions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alexandr7035.gitstat.data.ContributionsRepository
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContributionsViewModel @Inject constructor(private val repository: ContributionsRepository): ViewModel() {

    fun getContributionDaysLiveData(): LiveData<List<ContributionDayEntity>> {
        return repository.getAllContributionsLiveData()
    }

    fun getContributionYearsLiveData(): LiveData<List<ContributionsYearWithDays>> {
        return repository.getContributionYearsLiveData()
    }
}