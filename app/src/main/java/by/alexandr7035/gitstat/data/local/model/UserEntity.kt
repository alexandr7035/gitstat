package by.alexandr7035.gitstat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity(
    @PrimaryKey
    var id: Int,
    var login: String,
    var avatar_url: String,
    var name: String,
    var location: String,

    var public_repos_count: Int,
    var private_repos_count: Int,
    var total_repos_count: Int,

    var followers: Int,
    var following: Int,

    var created_at: Long,
    var updated_at: Long
)