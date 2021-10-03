package com.alexandr7035.gitstat.data.local.mappers

import com.alexandr7035.gitstat.core.Mapper
import com.alexandr7035.gitstat.core.TimeHelper
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYear
import javax.inject.Inject

// Transform list of contribution days to years
class ContributionsDaysToYearsMapper @Inject constructor(private val timeHelper: TimeHelper): Mapper<List<ContributionDayEntity>, List<ContributionsYear>> {

    override fun transform(data: List<ContributionDayEntity>): List<ContributionsYear> {
        val years = ArrayList<ContributionsYear>()

        // Get start and end end for contributions list
        val startYear = timeHelper.getYearFromUnixDate(data[0].date)
        val endYear = timeHelper.getYearFromUnixDate(data[data.size - 1].date)

        for (year in startYear..endYear) {
            val yearContributions = data.filter {
                val contributionYear = timeHelper.getYearFromUnixDate(it.date)
                contributionYear == year
            }
            years.add(ContributionsYear(year, yearContributions))
        }

        return years
    }
}