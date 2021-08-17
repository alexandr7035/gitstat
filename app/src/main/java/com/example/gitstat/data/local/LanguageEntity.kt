package com.example.gitstat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages")
class LanguageEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
    var reposCount: Int = 0)