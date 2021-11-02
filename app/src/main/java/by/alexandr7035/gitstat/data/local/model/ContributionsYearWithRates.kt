package by.alexandr7035.gitstat.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class ContributionsYearWithRates(
    @Embedded
    val year: ContributionsYearEntity,

    @Relation(parentColumn = "id", entityColumn = "yearId", entity = ContributionRateEntity::class)
    val contributionRates: List<ContributionRateEntity>)