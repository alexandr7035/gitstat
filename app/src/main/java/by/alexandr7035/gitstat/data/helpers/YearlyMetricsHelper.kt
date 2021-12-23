package by.alexandr7035.gitstat.data.helpers

import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates
import by.alexandr7035.gitstat.data.local.preferences.AppPreferences
import javax.inject.Inject
import kotlin.math.round

interface YearlyMetricsHelper {
    // Contribution rate for certain year
    fun getAnnualContributionRate(yearData: ContributionsYearWithRates): Float

    // Value of total (not annual) contribution rate by the end of specified year
    fun getEndingTotalContributionRate(yearData: ContributionsYearWithDays): Float


    class Impl @Inject constructor(private val timeHelper: TimeHelper, private val appPreferences: AppPreferences): YearlyMetricsHelper {
        override fun getAnnualContributionRate(yearData: ContributionsYearWithRates): Float {
            return if (yearData.year.id == timeHelper.getCurrentYearForUnixDate(System.currentTimeMillis())) {
                val lastCacheSyncDate = appPreferences.getLastCacheSyncDate()
                yearData.contributionRates.findLast { it.date == timeHelper.getBeginningOfDayForUnixDate(lastCacheSyncDate) }?.rate ?: 0F
            } else {
                yearData.contributionRates[yearData.contributionRates.size - 1].rate
            }
        }

        override fun getEndingTotalContributionRate(yearData: ContributionsYearWithDays): Float {
            val contributionsCount = yearData.contributionDays.sumOf { it.count }

            return if (yearData.year.id == timeHelper.getCurrentYearForUnixDate(System.currentTimeMillis())) {
                // Get last contribution day
                val lastCacheSyncDate = appPreferences.getLastCacheSyncDate()
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
    }
}