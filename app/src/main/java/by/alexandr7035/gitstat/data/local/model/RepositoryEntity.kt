package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
class RepositoryEntity(

@PrimaryKey
var id: Int,
var name: String,
var nameWithOwner: String,

var description: String,
var websiteUrl: String,

var language: String,
var languageColor: String,

var isPrivate: Boolean,
var isArchived: Boolean,
var isFork: Boolean,

var stars: Int,

var created_at: Long)