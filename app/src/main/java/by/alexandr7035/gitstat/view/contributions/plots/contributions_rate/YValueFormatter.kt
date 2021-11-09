package by.alexandr7035.gitstat.view.contributions.plots.contributions_rate

import com.github.mikephil.charting.formatter.ValueFormatter

class YValueFormatter(private val minValue: Float, private val maxValue: Float): ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return if (value == minValue || value == maxValue)
        // FIXME 2 digits after .
            value.toString()
        else
            ""
    }
}