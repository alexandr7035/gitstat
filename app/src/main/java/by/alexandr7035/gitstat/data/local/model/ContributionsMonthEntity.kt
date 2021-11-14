package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contribution_months")
data class ContributionsMonthEntity(
    // Save here "year"+ "month number".
    // E.g. 2020 + 01 -> 202001
    @PrimaryKey
    val id: Int
)