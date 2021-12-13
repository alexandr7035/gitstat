package by.alexandr7035.gitstat.core.extensions

import by.alexandr7035.gitstat.view.contributions.plots.LinePlotFill
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

fun LineChart.setupYearLineChartView(xValueFormatter: ValueFormatter?, yValueFormatter: ValueFormatter?) {
    // Disable legend
    legend.form = Legend.LegendForm.NONE

    // Disable scaling
    setScaleEnabled(false)

    // Disable description
    description.isEnabled = false

    // Disable right axis
    axisRight.isEnabled = false

    // Setup Y axis
    axisLeft.setDrawGridLines(false)
    axisLeft.axisMinimum = 0f
    axisLeft.textSize = 16f
    axisLeft.valueFormatter = yValueFormatter

    // Setup X axis
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setDrawGridLines(false)
    xAxis.textSize = 16f
    xAxis.valueFormatter = xValueFormatter
}


fun LineChart.setChartData(dataset: LineDataSet, linePlotFill: LinePlotFill) {

    // Fill only from zero point
    dataset.fillFormatter = IFillFormatter { dataSet, dataProvider -> 0f }

    dataset.apply {
        setDrawFilled(true)
        setDrawCircles(false)
        setDrawValues(false)
        color = linePlotFill.lineColor
        fillDrawable = linePlotFill.fillDrawable
    }

    data = LineData(dataset)
    data.isHighlightEnabled = false

}