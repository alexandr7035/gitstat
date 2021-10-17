package com.alexandr7035.gitstat.view.contributions.plots

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class DateMonthsValueFormatter: ValueFormatter() {
    override fun getFormattedValue(value: Float): String {

        // Example: convert "2020-02" to Feb
        val format = SimpleDateFormat("MMM", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")

        return format.format(value.toLong()).toString()
    }
}