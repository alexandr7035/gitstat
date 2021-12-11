package by.alexandr7035.gitstat.view.contributions.plots.contributions_types

import android.content.Context
import androidx.core.content.ContextCompat
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.data.local.model.ContributionTypesEntity
import kotlin.math.ceil

// FIXME architecture
class ContributionTypesListToRecyclerItemsMapper private constructor() {

    companion object {

        fun map(data: List<ContributionTypesEntity>, context: Context): List<TypesItem> {

            // Count types for all of the years
            val commits = data.sumOf { it.commitContributions }
            val issues = data.sumOf { it.issueContributions }
            val pullRequests = data.sumOf { it.pullRequestContributions }
            val reviews = data.sumOf { it.pullRequestReviewContributions }
            val repositories = data.sumOf { it.repositoryContributions }
            val unknown = data.sumOf { it.unknownContributions }
            val total = data.sumOf { it.totalContributions }

            return listOf(
                TypesItem(
                    label = "Commits",
                    count = commits,
                    percentage = getPercentage(commits / total.toFloat() * 100),
                    color = ContextCompat.getColor(context, R.color.color_commits)
                ),
                TypesItem(
                    label = "Repositories",
                    count = repositories,
                    percentage = getPercentage(repositories / total.toFloat() * 100),
                    color = ContextCompat.getColor(context, R.color.color_repositories)
                ),
                TypesItem(
                    label = "Issues",
                    count = issues,
                    percentage = getPercentage(issues / total.toFloat() * 100),
                    color = ContextCompat.getColor(context, R.color.color_issues)
                ),
                TypesItem(
                    label = "Pull requests",
                    count = pullRequests,
                    percentage = getPercentage(pullRequests / total.toFloat() * 100),
                    color = ContextCompat.getColor(context, R.color.color_pull_requests)
                ),
                TypesItem(
                    label = "Code reviews",
                    count = reviews,
                    percentage = getPercentage(reviews / total.toFloat() * 100),
                    color = ContextCompat.getColor(context, R.color.color_code_reviews)
                ),

                TypesItem(
                    label = "Unknown",
                    count = unknown,
                    percentage = getPercentage(unknown / total.toFloat() * 100),
                    color = ContextCompat.getColor(context, R.color.color_unknown_contributions)
                )
            )

        }


        private fun getPercentage(value: Float): String {
            val percentage = ceil(value * 100) / 100
            return "$percentage%"
        }

    }
}