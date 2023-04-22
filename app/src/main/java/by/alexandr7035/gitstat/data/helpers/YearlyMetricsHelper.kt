package by.alexandr7035.gitstat.data.helpers

import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates

interface YearlyMetricsHelper {
    // Contribution rate for certain year
    fun getAnnualContributionRate(yearData: ContributionsYearWithRates): Float

    // Value of total (not annual) contribution rate by the end of specified year
    fun getEndingTotalContributionRate(yearData: ContributionsYearWithDays): Float
}