package com.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contribution_years")
data class ContributionsYearEntity(
    @PrimaryKey
    var id: Int)
