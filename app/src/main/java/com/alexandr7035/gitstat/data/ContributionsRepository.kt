package com.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.core.TimeHelper
import com.alexandr7035.gitstat.data.local.dao.ContributionsDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsRatioEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates
import javax.inject.Inject

// FIXME use interface
class ContributionsRepository @Inject constructor(private val dao: ContributionsDao, private val timeHelper: TimeHelper) {

    fun getAllContributionsLiveData(): LiveData<List<ContributionDayEntity>> {
        return dao.getContributionDaysLiveData()
    }

    fun getContributionYearsWithDaysLiveData(): LiveData<List<ContributionsYearWithDays>> {
        return dao.getContributionYearsWithDaysLiveData()
    }

    fun getContributionYearsWithRatesLiveData(): LiveData<List<ContributionsYearWithRates>> {
        return dao.getContributionYearsWithRatesCache()
    }

    fun getContributionsRatioLiveData(): LiveData<List<ContributionsRatioEntity>> {
        return dao.getContributionRatiosLiveData()
    }


    // Simply calculate for previous years
    // For the current year need to detect current date
    fun getLastContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return if (yearData.year.id == timeHelper.getCurrentYearForUnixDate(System.currentTimeMillis())) {
            yearData.contributionRates.findLast { it.date == timeHelper.getBeginningOfDayForUnixDate(System.currentTimeMillis()) }?.rate ?: 0F
        } else {
            yearData.contributionRates[yearData.contributionRates.size - 1].rate
        }
    }

    fun getMaxContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return yearData.contributionRates.maxByOrNull { it.rate }?.rate ?: 0f
    }

}