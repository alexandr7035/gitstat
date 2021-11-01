package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "contribution_years")
data class ContributionsYearEntity(
    @PrimaryKey
    var id: Int): Serializable
