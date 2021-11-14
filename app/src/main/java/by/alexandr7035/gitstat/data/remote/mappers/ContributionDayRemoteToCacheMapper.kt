package by.alexandr7035.gitstat.data.remote.mappers

import android.graphics.Color
import by.alexandr7035.gitstat.apollo.ContributionsQuery
import by.alexandr7035.gitstat.core.Mapper
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import javax.inject.Inject

class ContributionDayRemoteToCacheMapper @Inject constructor(private val timeHelper: TimeHelper): Mapper<ContributionsQuery.ContributionDay, ContributionDayEntity> {
    override fun transform(data: ContributionsQuery.ContributionDay): ContributionDayEntity {
        val dateStr = data.date as String

        val unixDate = timeHelper.getUnixDateFrom_yyyyMMdd(dateStr)
        val year = timeHelper.getYearFromUnixDate(unixDate)
        val month = timeHelper.get_yyyyMM_fromUnixDate(unixDate).toInt()

        val intColor = Color.parseColor(data.color)

        return ContributionDayEntity(
            count = data.contributionCount,
            date = unixDate,
            yearId = year,
            color = intColor,
            monthId = month
        )
    }

}