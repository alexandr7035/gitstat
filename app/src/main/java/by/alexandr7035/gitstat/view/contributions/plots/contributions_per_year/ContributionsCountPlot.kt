package by.alexandr7035.gitstat.view.contributions.plots.contributions_per_year

import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import by.alexandr7035.gitstat.extensions.setupYAxisValuesForContributions
import by.alexandr7035.gitstat.view.contributions.plots.DateMonthsValueFormatter
import by.alexandr7035.gitstat.view.contributions.plots.LinePlotFill
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter

class ContributionsCountPlot {
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

            description.isEnabled = false
        }
    }

    fun setYearData(chart: LineChart, yearData: ContributionsYearWithDays) {

        val entries = ArrayList<Entry>()

        yearData.contributionDays.forEach { contributionDay ->
            entries.add(Entry(contributionDay.date.toFloat(), contributionDay.count.toFloat()))
        }

        val dataset = LineDataSet(entries, "")

        // Fill only from zero point
        dataset.fillFormatter = IFillFormatter { dataSet, dataProvider -> 0f }
        val plotFill = LinePlotFill.getPlotFillForYear(chart.context, yearData.year.id)

        dataset.apply {
            setDrawFilled(true)
            setDrawCircles(false)
            setDrawValues(false)
            color = plotFill.lineColor
            fillDrawable = plotFill.fillDrawable
        }

        val lineData = LineData(dataset)

        val topDay = yearData.contributionDays.maxByOrNull {
            it.count
        }

        chart.apply {
            data = lineData
            data.isHighlightEnabled = false

            axisLeft.setupYAxisValuesForContributions(topValue = topDay!!.count)
        }

        // Update chart
        chart.invalidate()
    }
}