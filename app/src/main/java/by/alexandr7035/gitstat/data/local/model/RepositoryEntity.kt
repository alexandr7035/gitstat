package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.alexandr7035.gitstat.core.Language

@Entity(tableName = "repositories")
class RepositoryEntity(

    @PrimaryKey
var id: Int,
    var name: String,
    var nameWithOwner: String,
    var parentNameWithOwner: String,

    var description: String,
    var websiteUrl: String,

    var primaryLanguage: String,
    var primaryLanguageColor: String,

    var languages: List<RepoLanguage>,

    var isPrivate: Boolean,
    var isArchived: Boolean,
    var isFork: Boolean,

    var stars: Int,

    var created_at: Long,
    var updated_at: Long,

    var diskUsageKB: Int,

    val topics: ArrayList<String>)