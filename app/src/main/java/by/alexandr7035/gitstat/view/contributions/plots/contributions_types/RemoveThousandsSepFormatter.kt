package by.alexandr7035.gitstat.view.contributions.plots.contributions_types

import com.github.mikephil.charting.formatter.ValueFormatter

// Simple solution to remove space like "1 000" in thousands
// Do not use with Floats!de
class RemoveThousandsSepFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toInt().toString()
    }
}