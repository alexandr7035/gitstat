package com.alexandr7035.gitstat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity(
    @PrimaryKey
    var id: Long,
    var login: String,
    var avatar_url: String,
    var name: String,
    var location: String,

    var public_repos: Long,
    var total_private_repos: Long,

    var followers: Long,
    var following: Long,

    var created_at: Long,
    var updated_at: Long,

    var cache_updated_at: Long
)