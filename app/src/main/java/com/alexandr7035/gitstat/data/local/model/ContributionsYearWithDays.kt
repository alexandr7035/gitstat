package com.alexandr7035.gitstat.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class ContributionsYearWithDays(
    @Embedded
    val year: ContributionsYearEntity,

    @Relation(parentColumn = "id", entityColumn = "yearId", entity = ContributionDayEntity::class)
    val contributionDays: List<ContributionDayEntity>
): Serializable