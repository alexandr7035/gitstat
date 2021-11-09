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


