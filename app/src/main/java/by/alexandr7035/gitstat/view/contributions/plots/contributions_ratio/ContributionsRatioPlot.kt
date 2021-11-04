package by.alexandr7035.gitstat.view.contributions.plots.contributions_ratio

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.data.local.model.ContributionsRatioEntity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.math.ceil

class ContributionsRatioPlot {

    // Use linked to save original order in derived diagramColors list
    private val colors = LinkedHashMap<String, Int>()

    fun setupPlot(chartView: BarChart, ratioData: List<ContributionsRatioEntity>) {
        initColors(chartView.context)
        setupUI(chartView)
        setData(chartView, ratioData)
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

    private fun setData(chartView: BarChart, ratioData: List<ContributionsRatioEntity>) {

        val commits = ratioData.sumOf { it.commitContributions }
        val issues = ratioData.sumOf { it.issueContributions }
        val pullRequests = ratioData.sumOf { it.pullRequestContributions }
        val reviews = ratioData.sumOf { it.pullRequestReviewContributions }
        val repositories = ratioData.sumOf { it.repositoryContributions }
        val unknown = ratioData.sumOf { it.unknownContributions }
        val total = ratioData.sumOf { it.totalContributions }

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
        val leftAxisParams = ContributionsRatioLeftAxisParams.getParamsForContributionYearCard(max)
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


    fun getRatioLegendItems(chartView: BarChart, ratioData: List<ContributionsRatioEntity>): List<RatioLegendItem> {

        // FIXME dry
        val commits = ratioData.sumOf { it.commitContributions }
        val issues = ratioData.sumOf { it.issueContributions }
        val pullRequests = ratioData.sumOf { it.pullRequestContributions }
        val reviews = ratioData.sumOf { it.pullRequestReviewContributions }
        val repositories = ratioData.sumOf { it.repositoryContributions }
        val unknown = ratioData.sumOf { it.unknownContributions }
        val total = ratioData.sumOf { it.totalContributions }


        return listOf(
            RatioLegendItem(
                label = "Commits",
                count = commits,
                percentage = getPercentage(commits / total.toFloat() * 100),
                color = colors["Commits"]!!
            ),
            RatioLegendItem(
                label = "Repositories",
                count = repositories,
                percentage = getPercentage(repositories / total.toFloat() * 100),
                color = colors["Repositories"]!!
            ),
            RatioLegendItem(
                label = "Issues",
                count = issues,
                percentage = getPercentage(issues / total.toFloat() * 100),
                color = colors["Issues"]!!
            ),
            RatioLegendItem(
                label = "Pull requests",
                count = pullRequests,
                percentage = getPercentage(pullRequests / total.toFloat() * 100),
                color = colors["Pull requests"]!!
            ),
            RatioLegendItem(
                label = "Code reviews",
                count = reviews,
                percentage = getPercentage(reviews / total.toFloat() * 100),
                color = colors["Code reviews"]!!
            ),

            // FIXME
            // Needs total count
            RatioLegendItem(
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
