package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contributions")
class ContributionDayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long,
    val count: Int,
    val color: Int,
    val yearId: Int,
    val monthId: Int
)