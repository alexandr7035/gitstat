package by.alexandr7035.gitstat.extensions

import android.graphics.Color
import android.graphics.Typeface
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

fun BarChart.setupHorizontalBarChart(valueFormatter: ValueFormatter?) {
   // Disable legend and description
   // Use custom legend based on RecyclerView
   description.isEnabled = false
   legend.isEnabled = false

    // Disable scaling
    setScaleEnabled(false)

    // Disable xAxis and axisRight
    // NOTE: as plot is rotated to HORIZONTAL,
    // axisLeft is the primary axis (normally it would be an xAxis)
    xAxis.isEnabled = false
    axisRight.isEnabled = false

    // Setup left axis
    axisLeft.textSize = 16f
    axisLeft.setDrawAxisLine(false)
    axisLeft.setDrawGridLines(false)
    axisLeft.valueFormatter = valueFormatter
    // Space between axis and labels
    axisLeft.yOffset = 10f
}


fun BarChart.setChartData(dataset: BarDataSet) {

    dataset.apply {
        setTouchEnabled(false)
        valueTextSize = 20f
        valueTextColor = Color.WHITE
        valueTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
    }

    data = BarData(dataset)
    data.setDrawValues(false)
}