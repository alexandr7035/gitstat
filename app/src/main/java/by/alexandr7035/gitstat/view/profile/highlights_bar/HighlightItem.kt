package by.alexandr7035.gitstat.view.profile.highlights_bar

import androidx.annotation.ColorRes

data class HighlightItem(
    val metricValue: String,
    val metricText: String,
    @ColorRes val accentColor: Int
)
