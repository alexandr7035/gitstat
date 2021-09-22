package com.alexandr7035.gitstat.data.remote.mappers

import com.alexandr7035.gitstat.core.Mapper
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.data.remote.model.RepositoryModel
import java.text.SimpleDateFormat
import java.util.*

class RepositoryRemoteToCacheMapper: Mapper<RepositoryModel, RepositoryEntity> {
    override fun transform(data: RepositoryModel): RepositoryEntity {
        // Do NOT fix. Can receive null from API.
        if (data.language == null) {
            data.language = "Unknown"
        }

        // Convert string dates to timestamp
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")
        val createdDate = format.parse(data.created_at)!!.time

        return RepositoryEntity(
            id = data.id,
            name = data.name,
            language = data.language,
            isPrivate = data.private,
            fork = data.fork,
            stars = data.stargazers_count,
            created_at = createdDate,
            archived = data.archived
        )
    }
}