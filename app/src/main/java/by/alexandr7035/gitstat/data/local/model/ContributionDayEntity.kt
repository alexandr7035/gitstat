package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contributions")
class ContributionDayEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var date: Long,
    var count: Int,
    var yearId: Int)