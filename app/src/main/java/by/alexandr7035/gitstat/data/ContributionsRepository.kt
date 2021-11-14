package by.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import by.alexandr7035.gitstat.core.KeyValueStorage
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.dao.ContributionsDao
import by.alexandr7035.gitstat.data.local.model.*
import javax.inject.Inject
import kotlin.math.round


class ContributionsRepository @Inject constructor(
    private val dao: ContributionsDao,
    private val timeHelper: TimeHelper,
    private val keyValueStorage: KeyValueStorage
) {

    fun getAllContributionsLiveData(): LiveData<List<ContributionDayEntity>> {
        return dao.getContributionDaysLiveData()
    }

    fun getContributionYearsWithDaysLiveData(): LiveData<List<ContributionsYearWithDays>> {
        return dao.getContributionYearsWithDaysLiveData()
    }

    fun getContributionYearsWithRatesLiveData(): LiveData<List<ContributionsYearWithRates>> {
        return dao.getContributionYearsWithRatesCache()
    }

    fun getContributionYearWithMonthsLiveData(yearId: Int): LiveData<ContributionYearWithMonths> {
        return dao.getContributionYearWithMonthsLiveData(yearId)
    }

    fun getContributionTypesLiveData(): LiveData<List<ContributionTypesEntity>> {
        return dao.getContributionTypesLiveData()
    }


    // Simply calculate for previous years
    // For the current year need to detect last contribution year
    fun getLastTotalContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return if (yearData.year.id == timeHelper.getCurrentYearForUnixDate(System.currentTimeMillis())) {
            val lastCacheSyncDate = keyValueStorage.getLastCacheSyncDate()
            yearData.contributionRates.findLast { it.date == timeHelper.getBeginningOfDayForUnixDate(lastCacheSyncDate) }?.rate ?: 0F
        } else {
            yearData.contributionRates[yearData.contributionRates.size - 1].rate
        }
    }

    fun getContributionRateForYear(yearData: ContributionsYearWithDays): Float {

        val contributionsCount = yearData.contributionDays.sumOf { it.count }

        return if (yearData.year.id == timeHelper.getCurrentYearForUnixDate(System.currentTimeMillis())) {
            // Get last contribution day
            val lastCacheSyncDate = keyValueStorage.getLastCacheSyncDate()
            val lastContributedDay = yearData.contributionDays.findLast {
                it.date == timeHelper.getBeginningOfDayForUnixDate(lastCacheSyncDate)
            }
            val lastContributedDayPosition = yearData.contributionDays.lastIndexOf(lastContributedDay)
            // Slice of days from the beginning of the year to last contribution date
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