package com.alexandr7035.gitstat.data.remote.mappers

import com.alexandr7035.gitstat.apollo.ContributionsQuery
import com.alexandr7035.gitstat.core.Mapper
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import java.text.SimpleDateFormat
import java.util.*

class ContributionDayRemoteToCacheMapper: Mapper<ContributionsQuery.ContributionDay, ContributionDayEntity> {
    override fun transform(data: ContributionsQuery.ContributionDay): ContributionDayEntity {

        val dateStr = data.date as String

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")

        val dateLong = format.parse(dateStr)!!.time

        return ContributionDayEntity(count = data.contributionCount, date = dateLong)
    }

}