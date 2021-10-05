package com.alexandr7035.gitstat.data.local.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
class RepositoryEntity(
//    @PrimaryKey
//    var id: Long,
//    var name: String,
//    var isPrivate: Boolean,
//    var fork: Boolean,
//    var language: String,
//    var stars: Long,
//    var created_at: Long,
//    var archived: Boolean)

@PrimaryKey
var id: Int,
var name: String,
var language: String,
var languageColor: String,

var isPrivate: Boolean,
var isArchived: Boolean,
var isFork: Boolean,

var stars: Int,

var created_at: Long)