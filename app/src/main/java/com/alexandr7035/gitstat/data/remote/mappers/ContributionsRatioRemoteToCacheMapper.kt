package com.alexandr7035.gitstat.data.remote.mappers

import com.alexandr7035.gitstat.apollo.ContributionsRatioQuery
import com.alexandr7035.gitstat.core.Mapper
import com.alexandr7035.gitstat.core.TimeHelper
import com.alexandr7035.gitstat.data.local.model.ContributionsRatioEntity
import javax.inject.Inject

class ContributionsRatioRemoteToCacheMapper @Inject constructor(private val timeHelper: TimeHelper): Mapper<ContributionsRatioQuery.Data, ContributionsRatioEntity> {
    override fun transform(data: ContributionsRatioQuery.Data): ContributionsRatioEntity {

        // Detect first day of a ratio period
        val unixDate = timeHelper.getUnixDateFrom_yyyyMMdd(data.viewer.contributionsCollection.contributionCalendar.weeks[0].firstDay as String)
        val year = timeHelper.getYearFromUnixDate(unixDate)

        return ContributionsRatioEntity(
            totalCommitContributions = data.viewer.contributionsCollection.totalCommitContributions,
            totalIssueContributions = data.viewer.contributionsCollection.totalIssueContributions,
            totalPullRequestContributions = data.viewer.contributionsCollection.totalPullRequestContributions,
            totalPullRequestReviewContributions = data.viewer.contributionsCollection.totalPullRequestReviewContributions,
            totalRepositoryContributions = data.viewer.contributionsCollection.totalRepositoryContributions,
            yearId = year
        )
    }
}