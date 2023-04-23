package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
class RepositoryEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val nameWithOwner: String,
    val parentNameWithOwner: String,
    val description: String,
    val websiteUrl: String,
    val primaryLanguage: String,
    val primaryLanguageColor: String,
    val languages: List<RepoLanguage>,
    val isPrivate: Boolean,
    val isArchived: Boolean,
    val isFork: Boolean,
    val stars: Int,
    val created_at: Long,
    val updated_at: Long,
    val diskUsageKB: Int,
    val topics: ArrayList<String>
)