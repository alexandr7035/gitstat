package by.alexandr7035.gitstat.extensions

import com.github.mikephil.charting.components.YAxis
import kotlin.math.floor

fun YAxis.setupYAxisValuesForContributions(topValue: Int) {

    val maxValue = if (topValue % 5 == 0) {
        topValue.toFloat()
    } else {
        // Round to nearest number to the top dividable by 5
        (floor(topValue / 5F) + 1) * 5
    }

    axisMaximum = maxValue
    axisMinimum = 0f

    // Show label on every 5 step
    setLabelCount((maxValue / 5).toInt() + 1, true)
}


fun YAxis.setupYAxisValuesForContributionRate(topValue: Float) {
    axisMinimum = 0f
    axisMaximum = topValue

    // Show only 2 labels (min and max values)
    setLabelCount(2, true)
}


fun YAxis.setupYAxisValuesForContributionTypes(topValue: Int) {

    axisMaximum = if (topValue % 100 == 0) {
        topValue.toFloat()
    } else {
        (floor(topValue / 100F) + 1) * 100
    }

    axisMinimum = 0f

    setLabelCount(5, true)
}

