package com.alexandr7035.gitstat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
class RepositoryEntity(
    @PrimaryKey
    var id: Long,
    var name: String,
    var isPrivate: Boolean,
    var language: String)