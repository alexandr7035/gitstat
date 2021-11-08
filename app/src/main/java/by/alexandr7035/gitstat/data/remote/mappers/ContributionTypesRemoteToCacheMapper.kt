package by.alexandr7035.gitstat.data.remote.mappers

import by.alexandr7035.gitstat.apollo.ContributionTypesQuery
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.model.ContributionTypesEntity
import javax.inject.Inject


// The real number of contributions obtained from summarizing contribution days may differ
// from sum of them in ContributionTypesEntity due to API behaviour
// Some contributions are not included in any of 5 types (commits, issues, PRs, reviews, repositories) returned from API
// E.g the first contributions - account creation. Or contributions from deleted repos in some cases
// So we pass realTotalContributions in order to calculate the difference
// and store it in "unknown" field
class ContributionTypesRemoteToCacheMapper @Inject constructor(private val timeHelper: TimeHelper) {
    fun transform(data: ContributionTypesQuery.Data, realTotalContributionsCount: Int): ContributionTypesEntity {

        // Detect first day of a period
        val unixDate = timeHelper.getUnixDateFrom_yyyyMMdd(data.viewer.contributionsCollection.contributionCalendar.weeks[0].firstDay as String)
        val year = timeHelper.getYearFromUnixDate(unixDate)


        val totalCommitContributions = data.viewer.contributionsCollection.totalCommitContributions
        val totalIssueContributions = data.viewer.contributionsCollection.totalIssueContributions
        val totalPullRequestContributions = data.viewer.contributionsCollection.totalPullRequestContributions
        val totalPullRequestReviewContributions = data.viewer.contributionsCollection.totalPullRequestReviewContributions
        val totalRepositoryContributions = data.viewer.contributionsCollection.totalRepositoryContributions

        val totalContributions = totalCommitContributions + totalIssueContributions + totalPullRequestContributions + totalPullRequestReviewContributions + totalRepositoryContributions
        val unknownContributions = realTotalContributionsCount - totalContributions

        return ContributionTypesEntity(
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