package by.alexandr7035.gitstat.view.repositories.overview.scrollable_repos_bar

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

sealed class ReposBarItems {
    data class MetricCard(
        val repoName: String,
        val metricName: String,
        val metricValue: Int,
        @DrawableRes val iconResId: Int
    ) : ReposBarItems()

    data class PinnedRepo(
        val repoName: String,
        val repoLang: String,
        val repoLangColor: String,
        val stars: Int,
        @ColorRes var bgColorRes: Int
    ) : ReposBarItems()
}
