package com.alexandr7035.gitstat.data.remote.mappers

import com.alexandr7035.gitstat.core.Mapper
import com.alexandr7035.gitstat.data.local.model.UserEntity
import com.alexandr7035.gitstat.data.remote.model.UserModel
import java.text.SimpleDateFormat
import java.util.*

class UserRemoteToCacheMapper: Mapper<UserModel, UserEntity> {
    override fun transform(data: UserModel): UserEntity {

        // Convert string dates to timestamp
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")

        val createdDate = format.parse(data.created_at).time
        val updatedDate = format.parse(data.updated_at).time

        return UserEntity(
            id = data.id,
            name = data.name,
            location = data.location,
            login = data.login,
            avatar_url = data.avatar_url,
            public_repos = data.public_repos,
            total_private_repos = data.total_private_repos,
            followers = data.followers,
            following = data.following,
            created_at = createdDate,
            updated_at = updatedDate,

            cache_updated_at = System.currentTimeMillis()
        )
    }
}