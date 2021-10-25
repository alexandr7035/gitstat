package by.alexandr7035.gitstat.view.contributions.plots.contributions_per_year

import by.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import timber.log.Timber
import kotlin.math.floor

data class ContributionsCountYAxisParams(
    var minValue: Float = 0f,
    var maxValue: Float,
    var labelsCount: Int)
{

    companion object {
        // Need this method as there is no way to set axis step directly
        fun getParamsForContributionYearCard(year: ContributionsYearWithDays): ContributionsCountYAxisParams {
            // Find top contribution day
            val topDay = year.contributionDays.maxByOrNull {
                it.count
            }

            return if (topDay != null) {


                val maxValue = if (topDay.count % 5 == 0) {
                    topDay.count.toFloat()
                } else {
                    (floor(topDay.count / 5F) + 1) * 5
                }


                // Show label every 5th contribution
                // But no less than 2
                val labelsCount = (maxValue / 5).toInt() + 1

                Timber.d("${topDay.count} max, top value $maxValue, labels count $labelsCount")

                ContributionsCountYAxisParams(maxValue = maxValue, labelsCount = labelsCount)
            } else {
                // TODO check it on empty lists
                ContributionsCountYAxisParams(minValue = 0f, maxValue = 0f, labelsCount = 0)
            }
        }
    }

    override fun toString(): String {
        return "minValue $minValue, maxValue $maxValue, labelsCount $labelsCount"
    }
}