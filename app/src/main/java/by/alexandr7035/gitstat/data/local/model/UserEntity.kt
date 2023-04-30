package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity(
    @PrimaryKey
    val id: Int,
    val login: String,
    val avatar_url: String,
    val name: String,
    val location: String,

    val public_repos_count: Int,
    val private_repos_count: Int,
    val total_repos_count: Int,

    val followers: Int,
    val following: Int,

    val created_at: Long,
    val updated_at: Long
)