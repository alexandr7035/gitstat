package by.alexandr7035.gitstat.extensions

import android.graphics.Color
import android.graphics.Typeface
import android.service.autofill.Dataset
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet

fun BarChart.setupHorizontalBarChart() {
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
    // Space between axis and labels
    axisLeft.yOffset = 10f
}


fun BarChart.setChartData(dataset: BarDataSet, chartColors: List<Int>) {

    dataset.apply {
        colors = chartColors
        valueTextSize = 20f
        valueTextColor = Color.WHITE
        valueTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
    }

    data = BarData(dataset)
    data.setDrawValues(false)
}