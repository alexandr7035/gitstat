package by.alexandr7035.gitstat.core.extensions

import android.graphics.Color
import android.graphics.Typeface
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

fun PieChart.setupPieChartView(isLegendEnabled: Boolean = true) {
    setEntryLabelTextSize(16f)
    setDrawEntryLabels(false)
    description.isEnabled = false
    setCenterTextSize(30f)

    setExtraOffsets(0f, 0f, 0f, 0f)

    if (isLegendEnabled) {
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.isWordWrapEnabled = true
        legend.textSize = 20f
        legend.xEntrySpace = 20f
    }
    else {
        legend.isEnabled = false
    }

}

fun PieChart.setPieChartData(dataset: PieDataSet, valueFormatter: ValueFormatter) {
    dataset.valueTextSize = 20f
    dataset.valueTextColor = Color.BLACK
    dataset.valueTypeface = Typeface.defaultFromStyle(Typeface.BOLD)

    val pieData = PieData(dataset)
    pieData.setValueFormatter(valueFormatter)

    data = pieData
}