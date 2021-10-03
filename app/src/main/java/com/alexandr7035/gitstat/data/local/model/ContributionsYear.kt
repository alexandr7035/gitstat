package com.alexandr7035.gitstat.data.local.model

import java.io.Serializable

data class ContributionsYear(
    val year: Int,
    val days: List<ContributionDayEntity>) : Serializable