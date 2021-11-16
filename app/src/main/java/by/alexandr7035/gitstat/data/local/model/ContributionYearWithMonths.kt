package by.alexandr7035.gitstat.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

class ContributionYearWithMonths(
    @Embedded
    val year: ContributionsYearEntity,

    @Relation(parentColumn = "id", entityColumn = "yearId", entity = ContributionsMonthEntity::class)
    val contributionMonths: List<ContributionsMonthWithDays>
)