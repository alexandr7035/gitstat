package com.alexandr7035.gitstat.view.contributions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alexandr7035.gitstat.data.ContributionsRepository
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsRatioEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContributionsViewModel @Inject constructor(private val repository: ContributionsRepository): ViewModel() {

    fun getContributionDaysLiveData(): LiveData<List<ContributionDayEntity>> {
        return repository.getAllContributionsLiveData()
    }

    fun getContributionYearsLiveData(): LiveData<List<ContributionsYearWithDays>> {
        return repository.getContributionYearsWithDaysLiveData()
    }

    fun getContributionYearsWithRatesLiveData(): LiveData<List<ContributionsYearWithRates>> {
        return repository.getContributionYearsWithRatesLiveData()
    }

    fun getContributionsRatioLiveData(): LiveData<List<ContributionsRatioEntity>> {
        return repository.getContributionsRatioLiveData()
    }

    fun getMaxContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return repository.getMaxContributionRateForYear(yearData)
    }

    fun getLastContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return repository.getLastContributionRateForYear(yearData)
    }
}