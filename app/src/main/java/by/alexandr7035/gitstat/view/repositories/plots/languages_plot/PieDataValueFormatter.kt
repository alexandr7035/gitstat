package by.alexandr7035.gitstat.view.repositories.plots.languages_plot

import com.github.mikephil.charting.formatter.ValueFormatter

class PieDataValueFormatter: ValueFormatter() {
    // Remove decimal part from value
    override fun getFormattedValue(value: Float): String {
        return "" + value.toInt()
    }
}