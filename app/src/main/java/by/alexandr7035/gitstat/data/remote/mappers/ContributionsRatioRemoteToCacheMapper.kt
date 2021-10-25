package by.alexandr7035.gitstat.data.remote.mappers

import by.alexandr7035.gitstat.apollo.ContributionsRatioQuery
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.model.ContributionsRatioEntity
import javax.inject.Inject

// FIXME interface

// The real number of contributions obtained from summarizing contribution days may differ
// from sum of them in ContributionsRatioEntity due to API behaviour
// Some contributions are not included in ContributionsRatioEntity
// E.g the first contributions - account creation. Or contributions from deleted repos in some cases
// So we pass realTotalContributions in order to calculate the difference
// and store it in "unknown" field
class ContributionsRatioRemoteToCacheMapper @Inject constructor(private val timeHelper: TimeHelper) {
    fun transform(data: ContributionsRatioQuery.Data, realTotalContributionsCount: Int): ContributionsRatioEntity {

        // Detect first day of a ratio period
        val unixDate = timeHelper.getUnixDateFrom_yyyyMMdd(data.viewer.contributionsCollection.contributionCalendar.weeks[0].firstDay as String)
        val year = timeHelper.getYearFromUnixDate(unixDate)


        val totalCommitContributions = data.viewer.contributionsCollection.totalCommitContributions
        val totalIssueContributions = data.viewer.contributionsCollection.totalIssueContributions
        val totalPullRequestContributions = data.viewer.contributionsCollection.totalPullRequestContributions
        val totalPullRequestReviewContributions = data.viewer.contributionsCollection.totalPullRequestReviewContributions
        val totalRepositoryContributions = data.viewer.contributionsCollection.totalRepositoryContributions

        val totalRatioContributions = totalCommitContributions + totalIssueContributions + totalPullRequestContributions + totalPullRequestReviewContributions + totalRepositoryContributions
        val unknownContributions = realTotalContributionsCount - totalRatioContributions

        return ContributionsRatioEntity(
            commitContributions = totalCommitContributions,
            issueContributions = totalIssueContributions,
            pullRequestContributions = totalPullRequestContributions,
            pullRequestReviewContributions = totalPullRequestReviewContributions,
            repositoryContributions = totalRepositoryContributions,
            unknownContributions = unknownContributions,

            totalContributions = realTotalContributionsCount,
            yearId = year
        )
    }
}