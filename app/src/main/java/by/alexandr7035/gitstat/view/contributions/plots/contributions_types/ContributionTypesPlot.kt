package by.alexandr7035.gitstat.view.contributions.plots.contributions_types

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.data.local.model.ContributionTypesEntity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.math.ceil

class ContributionTypesPlot {

    // Use linked to save original order in derived diagramColors list
    private val colors = LinkedHashMap<String, Int>()

    fun setupPlot(chartView: BarChart, typesData: List<ContributionTypesEntity>) {
        initColors(chartView.context)
        setupUI(chartView)
        setData(chartView, typesData)
    }

    // This plot is rotated on 90 degrees
    // So that when apply the settings to left we mean the top
    private fun setupUI(chartView: BarChart) {
        chartView.description.isEnabled = false
        chartView.legend.isEnabled = false

        chartView.setScaleEnabled(false)

        chartView.xAxis.isEnabled = false
        chartView.axisRight.isEnabled = false

        chartView.axisLeft.setDrawGridLines(false)

        chartView.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartView.xAxis.textSize = 16f

        chartView.axisLeft.textSize = 16f
        chartView.axisLeft.setDrawAxisLine(false)
        chartView.axisLeft.axisMinimum = 0f

        // Space between axis and plot
        chartView.axisLeft.yOffset = 10f

        chartView.setExtraOffsets(10f,0f,30f,0f)
    }

    private fun setData(chartView: BarChart, typesData: List<ContributionTypesEntity>) {

        val commits = typesData.sumOf { it.commitContributions }
        val issues = typesData.sumOf { it.issueContributions }
        val pullRequests = typesData.sumOf { it.pullRequestContributions }
        val reviews = typesData.sumOf { it.pullRequestReviewContributions }
        val repositories = typesData.sumOf { it.repositoryContributions }
        val unknown = typesData.sumOf { it.unknownContributions }
        val total = typesData.sumOf { it.totalContributions }

        // FIXME refactoring
        val max = listOf(
            commits,
            issues,
            pullRequests,
            reviews,
            repositories,
            unknown
        ).maxByOrNull {
            it
        } ?: 10


        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, commits.toFloat()))
        entries.add(BarEntry(-1f, repositories.toFloat()))
        entries.add(BarEntry(-2f, issues.toFloat()))
        entries.add(BarEntry(-3f, pullRequests.toFloat()))
        entries.add(BarEntry(-4f, reviews.toFloat()))
        entries.add(BarEntry(-5f, unknown.toFloat()))

        // Map colors hashmap to list
        val diagramColors = colors.toList().map {
            it.second
        }

        val dataSet = BarDataSet(entries, "")
        dataSet.apply {
            colors = diagramColors
            valueTextSize = 20f
            valueTextColor = Color.WHITE
            valueTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }

        val chartData = BarData(dataSet)
        chartData.setDrawValues(false)

        // Update data
        val leftAxisParams = ContributionTypesLeftAxisParams.getParamsForContributionYearCard(max)
        chartView.apply {
            data = chartData
            axisLeft.axisMinimum = leftAxisParams.minValue
            axisLeft.axisMaximum = leftAxisParams.maxValue
            axisLeft.setLabelCount(leftAxisParams.labelsCount, true)
            axisLeft.valueFormatter = RemoveThousandsSepFormatter()
        }
        chartView.data = chartData

        chartView.invalidate()
    }


    fun getContributionTypesLegendItems(chartView: BarChart, typesData: List<ContributionTypesEntity>): List<TypesLegendItem> {

        // FIXME dry
        val commits = typesData.sumOf { it.commitContributions }
        val issues = typesData.sumOf { it.issueContributions }
        val pullRequests = typesData.sumOf { it.pullRequestContributions }
        val reviews = typesData.sumOf { it.pullRequestReviewContributions }
        val repositories = typesData.sumOf { it.repositoryContributions }
        val unknown = typesData.sumOf { it.unknownContributions }
        val total = typesData.sumOf { it.totalContributions }


        return listOf(
            TypesLegendItem(
                label = "Commits",
                count = commits,
                percentage = getPercentage(commits / total.toFloat() * 100),
                color = colors["Commits"]!!
            ),
            TypesLegendItem(
                label = "Repositories",
                count = repositories,
                percentage = getPercentage(repositories / total.toFloat() * 100),
                color = colors["Repositories"]!!
            ),
            TypesLegendItem(
                label = "Issues",
                count = issues,
                percentage = getPercentage(issues / total.toFloat() * 100),
                color = colors["Issues"]!!
            ),
            TypesLegendItem(
                label = "Pull requests",
                count = pullRequests,
                percentage = getPercentage(pullRequests / total.toFloat() * 100),
                color = colors["Pull requests"]!!
            ),
            TypesLegendItem(
                label = "Code reviews",
                count = reviews,
                percentage = getPercentage(reviews / total.toFloat() * 100),
                color = colors["Code reviews"]!!
            ),

            // FIXME
            // Needs total count
            TypesLegendItem(
                label = "Unknown",
                count = unknown,
                percentage = getPercentage(unknown / total.toFloat() * 100),
                color = colors["Unknown"]!!
            )
        )
    }


    private fun initColors(context: Context) {
        colors.apply {
            put("Commits", ContextCompat.getColor(context, R.color.color_commits))
            put("Repositories", ContextCompat.getColor(context, R.color.color_repositories))
            put("Issues", ContextCompat.getColor(context, R.color.color_issues))
            put("Pull requests", ContextCompat.getColor(context, R.color.color_pull_requests))
            put("Code reviews", ContextCompat.getColor(context, R.color.color_code_reviews))
            put("Unknown", ContextCompat.getColor(context, R.color.color_unknown_contributions))
        }
    }

    private fun getPercentage(value: Float): Float {
        return ceil(value * 100) / 100
    }
}
