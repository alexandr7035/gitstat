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

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")

        val dateLong = format.parse(dateStr)!!.time

        val year = timeHelper.getYearFromUnixDate(dateLong)

        return ContributionDayEntity(count = data.contributionCount, date = dateLong, yearId = year)
    }

}