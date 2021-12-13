package by.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import by.alexandr7035.gitstat.data.helpers.YearlyMetricsHelper
import by.alexandr7035.gitstat.data.local.dao.ContributionsDao
import by.alexandr7035.gitstat.data.local.model.*
import javax.inject.Inject


class ContributionsRepository @Inject constructor(
    private val dao: ContributionsDao,
    private val yearlyMetricsHelper: YearlyMetricsHelper
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

    fun getContributionYearsWithMonthsLiveData(): LiveData<List<ContributionYearWithMonths>> {
        return dao.getContributionYearsWithMonthsLiveData()
    }

    fun getContributionTypesLiveData(): LiveData<List<ContributionTypesEntity>> {
        return dao.getContributionTypesLiveData()
    }


    // Simply calculate for previous years
    // For the current year need to detect last contribution year
    fun getLastTotalContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return yearlyMetricsHelper.getAnnualContributionRate(yearData)
    }

    fun getContributionRateForYear(yearData: ContributionsYearWithDays): Float {
        return yearlyMetricsHelper.getEndingTotalContributionRate(yearData)
    }

    fun getMaxContributionRateForYear(yearData: ContributionsYearWithRates): Float {
        return yearData.contributionRates.maxByOrNull { it.rate }?.rate ?: 0f
    }

}