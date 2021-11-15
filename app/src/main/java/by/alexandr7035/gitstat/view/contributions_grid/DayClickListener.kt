package by.alexandr7035.gitstat.view.contributions_grid

import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity

// Pass clicks from cells -> months -> grid fragment using this interface
interface DayClickListener {
    fun onDayItemClick(contributionDay: ContributionDayEntity)
}