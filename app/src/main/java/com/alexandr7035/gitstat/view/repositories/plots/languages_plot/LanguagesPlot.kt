package com.alexandr7035.gitstat.view.repositories.plots.languages_plot

import android.graphics.Color
import android.graphics.Typeface
import com.alexandr7035.gitstat.core.Language
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.ArrayList

class LanguagesPlot {

    fun setupPlot(chart: PieChart) {

        // General chart settings
        chart.apply {
            setEntryLabelTextSize(16f)
            setDrawEntryLabels(false)
            description.isEnabled = false
            setCenterTextSize(30f)
        }

        chart.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            orientation = Legend.LegendOrientation.HORIZONTAL
            setDrawInside(false)
            isWordWrapEnabled = true
            textSize = 20f
            xEntrySpace = 20f
        }

    }

    fun setLanguagesData(chart: PieChart, languages: List<Language>, totalReposCount: Int) {
        // Diagram data. Colors must correspond entries in their order in list
        val entries = ArrayList<PieEntry>()
        val diagramColors: MutableList<Int> = ArrayList()

        languages.forEach { language ->
            entries.add(PieEntry(language.count.toFloat(), language.name))
            diagramColors.add(Color.parseColor(language.color))
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.apply {
            colors = diagramColors
            valueTextSize = 20f
            valueTextColor = Color.WHITE
            valueTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }

        val pieData = PieData(dataSet)
        // Remove decimal part from values
        pieData.setValueFormatter(PieDataValueFormatter())

        // Update data
        chart.apply {
            centerText = totalReposCount.toString()
            // Should be in the end to display legend correctly
            data = pieData
        }

        chart.invalidate()
    }
}