package com.alexandr7035.gitstat.view.contributions.plots

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.alexandr7035.gitstat.R

data class LinePlotFill(
    val lineColor: Int,
    val fillDrawable: Drawable?) {

    companion object {
        fun getPlotFillForYear(context: Context, year: Int): LinePlotFill {

            val color = getColorForYear(context, year)
            val drawable = ContextCompat.getDrawable(context, R.drawable.background_contributions_graph)
            drawable?.setTint(color)

//            return PlotFill(ContextCompat.getColor(context, R.color.contributions_color), ContextCompat.getDrawable(context, R.drawable.background_contributions_graph))
            return LinePlotFill(color, drawable)
        }

        private fun getColorForYear(context: Context, year: Int): Int {
            // Get color depending on the last digit in the year number
            // Remainder per 10
            return when (year % 10) {
                0 -> ContextCompat.getColor(context, R.color.year_0)
                1 -> ContextCompat.getColor(context, R.color.year_1)
                2 -> ContextCompat.getColor(context, R.color.year_2)
                3 -> ContextCompat.getColor(context, R.color.year_3)
                4 -> ContextCompat.getColor(context, R.color.year_4)
                5 -> ContextCompat.getColor(context, R.color.year_5)
                6 -> ContextCompat.getColor(context, R.color.year_6)
                7 -> ContextCompat.getColor(context, R.color.year_7)
                8 -> ContextCompat.getColor(context, R.color.year_8)
                9 -> ContextCompat.getColor(context, R.color.year_9)

                else -> ContextCompat.getColor(context, R.color.year_0)
            }
        }
    }
}