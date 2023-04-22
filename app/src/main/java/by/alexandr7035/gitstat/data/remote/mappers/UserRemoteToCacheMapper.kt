package by.alexandr7035.gitstat.data.remote.mappers

import by.alexandr7035.gitstat.apollo.ProfileQuery
import by.alexandr7035.gitstat.core.Mapper
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.model.UserEntity
import javax.inject.Inject

class UserRemoteToCacheMapper @Inject constructor(private val timeHelper: TimeHelper): Mapper<ProfileQuery.Data, UserEntity> {
    override fun map(data: ProfileQuery.Data): UserEntity {

        // FIXME research later
        val id = data.viewer.databaseId!!

        val login = data.viewer.login
        val avatarUrl = data.viewer.avatarUrl.toString()

        val name = data.viewer.name ?: ""
        val location = data.viewer.location ?: ""

        val publicReposCount = data.viewer.publicRepositories.totalCount
        val privateReposCount = data.viewer.privateRepositories.totalCount
        val totalReposCount = data.viewer.allRepositories.totalCount

        val followersCount = data.viewer.followers.totalCount
        val followingCount = data.viewer.following.totalCount

        val createdDate = timeHelper.getUnixDateFromISO8601(data.viewer.createdAt as String)
        val updatedDate = timeHelper.getUnixDateFromISO8601(data.viewer.updatedAt as String)


        return UserEntity(
            id = id,
            name = name,
            location = location,
            login = login,
            avatar_url = avatarUrl,

            public_repos_count = publicReposCount,
            private_repos_count = privateReposCount,
            total_repos_count = totalReposCount,

            followers = followersCount,
            following = followingCount,

            created_at = createdDate,
            updated_at = updatedDate,
        )
    }
}