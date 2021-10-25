package com.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.core.TimeHelper
import com.alexandr7035.gitstat.data.local.dao.ContributionsDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsRatioEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates
import javax.inject.Inject
import kotlin.math.round


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
    fun getLastTotalContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return if (yearData.year.id == timeHelper.getCurrentYearForUnixDate(System.currentTimeMillis())) {
            yearData.contributionRates.findLast { it.date == timeHelper.getBeginningOfDayForUnixDate(System.currentTimeMillis()) }?.rate ?: 0F
        } else {
            yearData.contributionRates[yearData.contributionRates.size - 1].rate
        }
    }

    fun getContributionRateForYear(yearData: ContributionsYearWithDays): Float {

        val contributionsCount = yearData.contributionDays.sumOf { it.count }

        return if (yearData.year.id == timeHelper.getCurrentYearForUnixDate(System.currentTimeMillis())) {
            // Get last contribution day (current day)
            val lastContributedDay = yearData.contributionDays.findLast {
                it.date == timeHelper.getBeginningOfDayForUnixDate(System.currentTimeMillis())
            }
            val lastContributedDayPosition = yearData.contributionDays.lastIndexOf(lastContributedDay)
            // Slice of days from the beginning of the year to current day
            val contributionDays = yearData.contributionDays.slice(0..lastContributedDayPosition)

            round(contributionsCount.toFloat() / contributionDays.size.toFloat() * 100) / 100F
        } else {
            round(contributionsCount.toFloat() / yearData.contributionDays.size.toFloat() * 100) / 100F
        }
    }

    fun getMaxContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return yearData.contributionRates.maxByOrNull { it.rate }?.rate ?: 0f
    }

}