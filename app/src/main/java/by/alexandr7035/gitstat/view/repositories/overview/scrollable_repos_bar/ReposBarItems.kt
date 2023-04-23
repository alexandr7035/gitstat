package by.alexandr7035.gitstat.view.repositories.overview.scrollable_repos_bar

import androidx.annotation.DrawableRes

sealed class ReposBarItems {
    data class MetricCard(
        val repoName: String,
        val metricName: String,
        val metricValue: Int,
        @DrawableRes val iconResId: Int
    ): ReposBarItems()

    data class PinnedRepo(
        val repoName: String
        // TODO color
    ): ReposBarItems()
}
