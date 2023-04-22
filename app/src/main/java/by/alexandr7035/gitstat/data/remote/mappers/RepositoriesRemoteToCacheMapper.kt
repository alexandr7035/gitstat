package by.alexandr7035.gitstat.data.remote.mappers

import by.alexandr7035.gitstat.apollo.RepositoriesQuery
import by.alexandr7035.gitstat.core.Mapper
import by.alexandr7035.gitstat.core.helpers.TimeHelper
import by.alexandr7035.gitstat.data.local.model.RepoLanguage
import by.alexandr7035.gitstat.data.local.model.RepositoryEntity
import by.alexandr7035.gitstat.core.extensions.debug
import timber.log.Timber
import javax.inject.Inject

class RepositoriesRemoteToCacheMapper @Inject constructor(private val timeHelper: TimeHelper): Mapper<RepositoriesQuery.Data, List<RepositoryEntity>> {

    override fun map(data: RepositoriesQuery.Data): List<RepositoryEntity> {

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

                    val languages = ArrayList<RepoLanguage>()

                    if (repo.languages?.edges != null) {

                        if (repo.languages.edges.isNotEmpty()) {

                            for (edge in repo.languages.edges) {

                                val nodeLanguage = when (edge?.node?.name) {
                                    null -> "Unknown"
                                    else -> edge.node.name
                                }

                                val nodeLanguageColor = when (edge?.node?.color) {
                                    null -> "#C3C3C3"
                                    else -> edge.node.color
                                }

                                val size = edge?.size ?: 0

                                languages.add(RepoLanguage(name = nodeLanguage, color = nodeLanguageColor, size = size))
                            }
                        }
                        else {
                            languages.add(RepoLanguage("Unknown", "#C3C3C3", size = 1))
                        }
                    }

                    val repository = RepositoryEntity(
                        id = repo.databaseId!!,
                        name = repo.name,
                        nameWithOwner = repo.nameWithOwner,
                        parentNameWithOwner = repo.parent?.nameWithOwner ?: "",

                        description = repo.description ?: "No repository description provided.",
                        websiteUrl = websiteUrl,
                        primaryLanguage = language,
                        primaryLanguageColor = languageColor,

                        isPrivate = repo.isPrivate,
                        isArchived = repo.isArchived,
                        isFork = repo.isFork,

                        stars = repo.stargazerCount,

                        created_at = timeHelper.getUnixDateFromISO8601(repo.createdAt as String),
                        updated_at = timeHelper.getUnixDateFromISO8601(repo.updatedAt as String),

                        diskUsageKB = repo.diskUsage ?: 0,

                        topics = topics,
                        languages = languages
                    )

                    cachedList.add(repository)
                }
            }

            return cachedList
        }
    }
}