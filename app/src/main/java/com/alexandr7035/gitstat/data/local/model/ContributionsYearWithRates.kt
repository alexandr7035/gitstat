package com.alexandr7035.gitstat.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class ContributionsYearWithRates(
    @Embedded
    val year: ContributionsYearEntity,

    @Relation(parentColumn = "id", entityColumn = "yearId", entity = ContributionRateEntity::class)
    val contributionRates: List<ContributionRateEntity>): Serializable