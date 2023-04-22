package by.alexandr7035.gitstat.data.remote.mappers

import by.alexandr7035.gitstat.apollo.ContributionsQuery
import by.alexandr7035.gitstat.core.Mapper
import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import javax.inject.Inject

class ContributionsDaysListRemoteToCacheMapper @Inject constructor(private val mapper: ContributionDayRemoteToCacheMapper): Mapper<ContributionsQuery.Data, List<ContributionDayEntity>> {
    override fun map(data: ContributionsQuery.Data): List<ContributionDayEntity> {

        val contributions = ArrayList<ContributionDayEntity>()

        for (week in data.viewer.contributionsCollection.contributionCalendar.weeks) {
            for (day in week.contributionDays) {
                contributions.add(mapper.map(day))
            }
        }

        return contributions
    }
}