package com.alexandr7035.gitstat.data.remote.mappers

import com.alexandr7035.gitstat.apollo.ContributionsQuery
import com.alexandr7035.gitstat.core.Mapper
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import javax.inject.Inject

class ContributionsDaysListRemoteToCacheMapper @Inject constructor(private val mapper: ContributionDayRemoteToCacheMapper): Mapper<ContributionsQuery.Data, List<ContributionDayEntity>> {
    override fun transform(data: ContributionsQuery.Data): List<ContributionDayEntity> {

        val contributions = ArrayList<ContributionDayEntity>()

        for (week in data.viewer.contributionsCollection.contributionCalendar.weeks) {
            for (day in week.contributionDays) {
//                Log.d("DEBUG_APOLLO", "$day")
                contributions.add(mapper.transform(day))
            }
        }

        return contributions
    }
}