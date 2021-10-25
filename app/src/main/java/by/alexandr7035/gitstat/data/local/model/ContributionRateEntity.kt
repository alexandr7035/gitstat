package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contribution_rates")
data class ContributionRateEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val rate: Float,
    val date: Long,
    var yearId: Int)