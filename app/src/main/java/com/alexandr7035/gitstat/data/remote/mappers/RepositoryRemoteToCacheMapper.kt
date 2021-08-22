package com.alexandr7035.gitstat.data.remote.mappers

import com.alexandr7035.gitstat.common.Mapper
import com.alexandr7035.gitstat.data.Repository
import com.alexandr7035.gitstat.data.local.RepositoryEntity
import com.alexandr7035.gitstat.data.remote.RepositoryModel

class RepositoryRemoteToCacheMapper: Mapper<RepositoryModel, RepositoryEntity> {
    override fun transform(data: RepositoryModel): RepositoryEntity {
        if (data.language == null) {
            data.language = "Unknown"
        }

        return RepositoryEntity(
            id = data.id,
            name = data.name,
            language = data.language,
            isPrivate = data.private
        )
    }
}