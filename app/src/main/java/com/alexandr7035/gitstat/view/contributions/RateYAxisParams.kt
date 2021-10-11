package com.alexandr7035.gitstat.view.contributions

import com.github.mikephil.charting.formatter.ValueFormatter


data class RateYAxisParams(
    val minValue: Float,
    val maxValue: Float,
    val labelCount: Int,
    val yAxisValueFormatter: ValueFormatter) {

    companion object {

        private const val minValue = 0f
        private const val labelCount = 2

        fun getRateYAxisParams(maxValue: Float): RateYAxisParams {
            return RateYAxisParams(minValue, maxValue, labelCount, YValueFormatter(minValue, maxValue))
        }
    }

    class YValueFormatter(private val minValue: Float, private val maxValue: Float): ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return if (value == minValue || value == maxValue)
                // FIXME 2 digits after .
                value.toString()
            else
                ""
        }
    }
}