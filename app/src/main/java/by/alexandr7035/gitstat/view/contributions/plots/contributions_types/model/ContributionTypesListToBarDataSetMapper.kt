package by.alexandr7035.gitstat.view.contributions.plots.contributions_types.model

import android.content.Context
import androidx.core.content.ContextCompat
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.data.local.model.ContributionTypesEntity
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class ContributionTypesListToBarDataSetMapper private constructor() {

    companion object {

        fun map(data: List<ContributionTypesEntity>, context: Context): BarDataSet {

            // Count types for all of the years
            val commits = data.sumOf { it.commitContributions }
            val issues = data.sumOf { it.issueContributions }
            val pullRequests = data.sumOf { it.pullRequestContributions }
            val reviews = data.sumOf { it.pullRequestReviewContributions }
            val repositories = data.sumOf { it.repositoryContributions }
            val unknown = data.sumOf { it.unknownContributions }

            // Change default order with negative values
            val entries = ArrayList<BarEntry>()
            entries.add(BarEntry(0f, commits.toFloat()))
            entries.add(BarEntry(-1f, repositories.toFloat()))
            entries.add(BarEntry(-2f, issues.toFloat()))
            entries.add(BarEntry(-3f, pullRequests.toFloat()))
            entries.add(BarEntry(-4f, reviews.toFloat()))
            entries.add(BarEntry(-5f, unknown.toFloat()))

            val colors = listOf(
                ContextCompat.getColor(context, R.color.color_commits),
                ContextCompat.getColor(context, R.color.color_repositories),
                ContextCompat.getColor(context, R.color.color_issues),
                ContextCompat.getColor(context, R.color.color_pull_requests),
                ContextCompat.getColor(context, R.color.color_code_reviews),
                ContextCompat.getColor(context, R.color.color_unknown_contributions)
            )

            val dataSet = BarDataSet(entries, "")
            dataSet.colors = colors

            return dataSet
        }
    }
}