package by.alexandr7035.gitstat.data.remote.mappers

import by.alexandr7035.gitstat.apollo.ContributionsQuery
import by.alexandr7035.gitstat.core.Mapper
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ContributionDayRemoteToCacheMapper @Inject constructor(private val timeHelper: TimeHelper): Mapper<ContributionsQuery.ContributionDay, ContributionDayEntity> {
    override fun transform(data: ContributionsQuery.ContributionDay): ContributionDayEntity {
        val dateStr = data.date as String

        val unixDate = timeHelper.getUnixDateFrom_yyyyMMdd(dateStr)
        val year = timeHelper.getYearFromUnixDate(unixDate)

        return ContributionDayEntity(count = data.contributionCount, date = unixDate, yearId = year)
    }

}