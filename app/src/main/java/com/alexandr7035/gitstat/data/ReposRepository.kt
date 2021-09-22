package com.alexandr7035.gitstat.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.core.Language
import com.alexandr7035.gitstat.core.ProgLangManager
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.data.remote.NetworkModule
import com.alexandr7035.gitstat.data.remote.mappers.RepositoryRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.model.RepositoryModel
import com.alexandr7035.gitstat.view.repositories_list.filters.ReposFilters
import com.google.gson.Gson
import javax.inject.Inject

class ReposRepository @Inject constructor(
    private val api: NetworkModule,
    private val dao: CacheDao,
    private val repoMapper: RepositoryRemoteToCacheMapper,
    private val appPreferences: AppPreferences,
    private val gson: Gson,
    private val langManager: ProgLangManager
) {
    private val syncStateLiveData = MutableLiveData<SyncStatus>()

    suspend fun syncRepositoriesData() {

        syncStateLiveData.postValue(SyncStatus.PENDING)

        try {
            val res = api.getRepositoriesData()

            if (res.isSuccessful) {
                syncStateLiveData.postValue(SyncStatus.SUCCESS)
                val reposList: List<RepositoryModel> = res.body()!!

                val cachedReposList = reposList.map { repositoryModel ->
                    repoMapper.transform(repositoryModel)
                }

                dao.clearRepositoriesCache()
                dao.insertRepositoriesCache(cachedReposList)
            } else {
                syncStateLiveData.postValue(SyncStatus.FAILED)
            }
        }

        catch (e: Exception) {
            ////Log.d(LOG_TAG, "exception ${e}")
            syncStateLiveData.postValue(SyncStatus.FAILED)
        }
    }

    fun getSyncStatusLiveData(): MutableLiveData<SyncStatus> {
        return syncStateLiveData
    }

    suspend fun fetchAllRepositoriesFromDb(): List<RepositoryEntity> {
        return dao.getRepositoriesCache()
    }

    suspend fun fetchActiveRepositoriesFromDb(): List<RepositoryEntity> {
        return dao.getActiveRepositoriesCache()
    }

    suspend fun fetchArchivedRepositoriesFromDb(): List<RepositoryEntity> {
        return dao.getArchivedRepositoriesCache()
    }


    // Filters
    fun getRepositoriesFilters(): ReposFilters {
        val filtersStr = appPreferences.repositoriesFilters

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
        appPreferences.repositoriesFilters = gson.toJson(filters)
    }


    // Prog languages
    fun getLanguagesForReposList(repos: List<RepositoryEntity>): List<Language> {
        return langManager.getLanguagesList(repos)
    }

}