package by.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import by.alexandr7035.gitstat.core.Language
import by.alexandr7035.gitstat.data.helpers.LanguagesHelper
import by.alexandr7035.gitstat.data.local.dao.RepositoriesDao
import by.alexandr7035.gitstat.data.local.model.RepositoryEntity
import by.alexandr7035.gitstat.data.local.preferences.AppPreferences
import by.alexandr7035.gitstat.view.repositories.filters.ReposFilters
import com.google.gson.Gson
import javax.inject.Inject

class ReposRepository @Inject constructor(
    private val dao: RepositoriesDao,
    private val appPreferences: AppPreferences,
    private val languagesHelper: LanguagesHelper,
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
        val filtersStr = appPreferences.getRepositoriesFilters()

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
        appPreferences.saveRepositoriesFilters(gson.toJson(filters))
    }


    // Prog languages for repo list
    fun getLanguagesForReposList(repos: List<RepositoryEntity>): List<Language> {
        return languagesHelper.getLanguagesForReposList(repos)
    }

}