package by.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import by.alexandr7035.gitstat.core.KeyValueStorage
import by.alexandr7035.gitstat.core.Language
import by.alexandr7035.gitstat.data.local.dao.RepositoriesDao
import by.alexandr7035.gitstat.data.local.model.RepositoryEntity
import by.alexandr7035.gitstat.view.repositories.filters.ReposFilters
import com.google.gson.Gson
import javax.inject.Inject

class ReposRepository @Inject constructor(
    private val dao: RepositoriesDao,
    private val keyValueStorage: KeyValueStorage,
    private val gson: Gson) {

    fun getRepositoriesLiveData(): LiveData<List<RepositoryEntity>>{
        return dao.getRepositoriesLiveData()
    }

    fun getRepositoryLiveData(repositoryId: Int): LiveData<RepositoryEntity> {
        return dao.getRepositoryLiveData(repositoryId)
    }

    suspend fun fetchAllRepositoriesFromDb(): List<RepositoryEntity> {
        return dao.getRepositories()
    }

    suspend fun fetchActiveRepositoriesFromDb(): List<RepositoryEntity> {
        return dao.getActiveRepositories()
    }

    suspend fun fetchArchivedRepositoriesFromDb(): List<RepositoryEntity> {
        return dao.getArchivedRepositories()
    }


    // Filters
    fun getRepositoriesFilters(): ReposFilters {
        val filtersStr = keyValueStorage.getRepositoriesFilters()

        return if (filtersStr == null) {
            // New instance with default params
            // See filters class
            ReposFilters()
        } else {
            // Load from persistent storage
            gson.fromJson(filtersStr, ReposFilters::class.java)
        }
    }


    fun saveRepositoriesFilters(filters: ReposFilters) {
        keyValueStorage.saveRepositoriesFilters(gson.toJson(filters))
    }


    // Prog languages
    // FIXME find more optimal way (too many iterations)
    fun getLanguagesForReposList(repos: List<RepositoryEntity>): List<Language> {
        val languagesList = ArrayList<Language>()

        repos.forEach {
            languagesList.add(Language(it.primaryLanguage, it.primaryLanguageColor, 0))
        }

        val trimmedLanguages = languagesList.distinct()

        repos.forEach { repo ->
            trimmedLanguages.forEach { language ->
                if (repo.primaryLanguage == language.name) {
                    language.count += 1
                }
            }
        }

        return trimmedLanguages.sortedBy {
            it.name
        }

    }

}