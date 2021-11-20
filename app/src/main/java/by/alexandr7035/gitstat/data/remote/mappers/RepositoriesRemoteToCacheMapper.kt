package by.alexandr7035.gitstat.data.remote.mappers

import by.alexandr7035.gitstat.apollo.RepositoriesQuery
import by.alexandr7035.gitstat.core.Mapper
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.model.RepositoryEntity
import by.alexandr7035.gitstat.extensions.debug
import timber.log.Timber
import javax.inject.Inject

class RepositoriesRemoteToCacheMapper @Inject constructor(private val timeHelper: TimeHelper): Mapper<RepositoriesQuery.Data, List<RepositoryEntity>> {

    override fun transform(data: RepositoriesQuery.Data): List<RepositoryEntity> {

        if (data.viewer.repositories.nodes == null) {
            return emptyList()
        }
        else {
            val cachedList = ArrayList<RepositoryEntity>()

            for (repo in data.viewer.repositories.nodes) {

                if (repo != null) {

                    val language = when (repo.primaryLanguage) {
                        null -> "Unknown"
                        else -> repo.primaryLanguage.name
                    }

                    val languageColor = when (repo.primaryLanguage) {
                        null -> "#C3C3C3"
                        else -> {
                            when (repo.primaryLanguage.color) {
                                null -> "#C3C3C3"
                                else -> repo.primaryLanguage.color
                            }
                        }
                    }

                    val websiteUrl = if (repo.homepageUrl != null) {
                        repo.homepageUrl as String
                    } else {
                        ""
                    }

                    val topics = ArrayList<String>()

                    if (repo.repositoryTopics.nodes != null) {
                        for (node in repo.repositoryTopics.nodes) {
                            if (node != null) {
                                topics.add(node.topic.name)
                            }
                        }
                    }

                    Timber.debug("topics $topics")

                    val repository = RepositoryEntity(
                        id = repo.databaseId!!,
                        name = repo.name,
                        nameWithOwner = repo.nameWithOwner,
                        description = repo.description ?: "No repository description provided.",
                        websiteUrl = websiteUrl,
                        language = language,
                        languageColor = languageColor,

                        isPrivate = repo.isPrivate,
                        isArchived = repo.isArchived,
                        isFork = repo.isFork,

                        stars = repo.stargazerCount,

                        created_at = timeHelper.getUnixDateFromISO8601(repo.createdAt as String),
                        updated_at = timeHelper.getUnixDateFromISO8601(repo.updatedAt as String),

                        diskUsageKB = repo.diskUsage ?: 0,

                        topics = topics
                    )

                    cachedList.add(repository)
                }
            }

            return cachedList
        }
    }
}