package by.alexandr7035.gitstat.view.contributions.plots.contributions_types

import kotlin.math.floor

data class ContributionTypesLeftAxisParams(
    var minValue: Float = 0f,
    var maxValue: Float,
    var labelsCount: Int)
{

    companion object {
        // Need this method as there is no way to set axis step directly
        fun getParamsForContributionYearCard(maxContributionsValue: Int): ContributionTypesLeftAxisParams {

            val maxPlotValue = if (maxContributionsValue % 100 == 0) {
                maxContributionsValue.toFloat()
            } else {
                (floor(maxContributionsValue / 100F) + 1) * 100
            }

            val labelCount = 5

            return ContributionTypesLeftAxisParams(minValue = 0f, maxValue = maxPlotValue, labelsCount = labelCount)

        }

    }

    override fun toString(): String {
        return "minValue $minValue, maxValue $maxValue, labelsCount $labelsCount"
    }
}