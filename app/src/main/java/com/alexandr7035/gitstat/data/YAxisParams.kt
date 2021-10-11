package com.alexandr7035.gitstat.data

import android.util.Log
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import kotlin.math.floor

data class YAxisParams(
    var minValue: Float = 0f,
    var maxValue: Float,
    var labelsCount: Int)
{

    companion object {
        // Need this method as there is no way to set axis step directly
        fun getParamsForContributionYearCard(year: ContributionsYearWithDays): YAxisParams {
            // Find top contribution day
            val topDay = year.contributionDays.maxByOrNull {
                it.count
            }

            return if (topDay != null) {


                val maxValue = if (topDay.count % 5 == 0) {
                    topDay.count.toFloat()
                }
                else {
                    (floor(topDay.count / 5F) + 1) * 5
                }


                // Show label every 5th contribution
                // But no less than 2
                val labelsCount = (maxValue / 5).toInt() + 1

                Log.d("DEBUG_TAG", "${topDay.count} max, top value ${maxValue}, labels count $labelsCount")

                YAxisParams(maxValue = maxValue, labelsCount = labelsCount)
            } else {
                // TODO check it on empty lists
                YAxisParams(minValue = 0f, maxValue = 0f, labelsCount = 0)
            }
        }
    }

    override fun toString(): String {
        return "minValue $minValue, maxValue $maxValue, labelsCount $labelsCount"
    }
}