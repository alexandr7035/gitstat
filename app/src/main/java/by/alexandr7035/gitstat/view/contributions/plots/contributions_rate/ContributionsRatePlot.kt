package by.alexandr7035.gitstat.view.contributions.plots.contributions_rate

import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithRates
import by.alexandr7035.gitstat.view.contributions.plots.DateMonthsValueFormatter
import by.alexandr7035.gitstat.view.contributions.plots.LinePlotFill
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter

class ContributionsRatePlot {
    fun setupPLot(chart: LineChart) {
        chart.apply {

            // Disable legend
            legend.form = Legend.LegendForm.NONE

            setScaleEnabled(false)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.textSize = 16f
            // Show months names
            xAxis.valueFormatter = DateMonthsValueFormatter()

            axisRight.setDrawLabels(false)
            axisRight.axisMinimum = 0f
            axisRight.setDrawGridLines(false)

            axisLeft.setDrawGridLines(false)
            axisLeft.axisMinimum = 0f
            axisLeft.textSize = 16f

            setExtraOffsets(10f,0f,10f,0f)

            description.isEnabled = false
        }
    }


    fun setYearData(chart: LineChart, yearData: ContributionsYearWithRates) {
        // YAxis params for the plot
        val maxContributionsRate = yearData.contributionRates.maxByOrNull { it.rate }?.rate
        val yAxisParams = RateYAxisParams.getRateYAxisParams(maxContributionsRate ?: 10f)

        // Get color params for the plot
        val plotFill = LinePlotFill.getPlotFillForYear(chart.context, yearData.year.id)

        // Plot entries
        val entries = ArrayList<Entry>()
        yearData.contributionRates.forEach { contributionRate ->
            entries.add(Entry(contributionRate.date.toFloat(), contributionRate.rate))
        }

        // Prepare plot dataset
        val dataset = LineDataSet(entries, "")
        // Fill only from zero point
        dataset.fillFormatter = IFillFormatter { dataSet, dataProvider -> 0f }

        dataset.apply {
            setDrawFilled(true)
            setDrawCircles(false)
            setDrawValues(false)

            color = plotFill.lineColor
            fillDrawable = plotFill.fillDrawable
        }
        val lineData = LineData(dataset)


        // Populate plot with data
        chart.apply {
            data = lineData
            data.isHighlightEnabled = false

            // Update axis params
            axisLeft.valueFormatter = yAxisParams.yAxisValueFormatter
            axisLeft.axisMinimum = yAxisParams.minValue
            axisLeft.axisMaximum = yAxisParams.maxValue
            axisLeft.setLabelCount(yAxisParams.labelCount, true)
        }

        // Update chart
        chart.invalidate()
    }
}