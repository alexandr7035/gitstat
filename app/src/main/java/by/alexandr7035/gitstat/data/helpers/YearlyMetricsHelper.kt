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
}