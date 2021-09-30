package com.alexandr7035.gitstat.view.contributions

import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity

data class ContributionsYear(
    val year: Int,
    val days: List<ContributionDayEntity>)