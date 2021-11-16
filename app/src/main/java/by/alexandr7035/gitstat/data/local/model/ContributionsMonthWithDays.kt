package by.alexandr7035.gitstat.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

class ContributionsMonthWithDays(
    @Embedded
    val month: ContributionsMonthEntity,

    @Relation(parentColumn = "id", entityColumn = "monthId", entity = ContributionDayEntity::class)
    val contributionDays: List<ContributionDayEntity>)