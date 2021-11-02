package by.alexandr7035.gitstat.view.contributions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.alexandr7035.gitstat.data.ContributionsRepository
import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import by.alexandr7035.gitstat.data.local.model.ContributionsRatioEntity
import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates
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

    fun getContributionYearsWithDaysLiveData(): LiveData<List<ContributionsYearWithDays>> {
        return repository.getContributionYearsWithDaysLiveData()
    }

    fun getContributionsRatioLiveData(): LiveData<List<ContributionsRatioEntity>> {
        return repository.getContributionsRatioLiveData()
    }

    fun getMaxContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return repository.getMaxContributionRateForYear(yearData)
    }

    fun getLastTotalContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return repository.getLastTotalContributionRateForYear(yearData)
    }

    fun getContributionRateForYear(yearContributions: ContributionsYearWithDays): Float {
        return repository.getContributionRateForYear(yearContributions)
    }
}