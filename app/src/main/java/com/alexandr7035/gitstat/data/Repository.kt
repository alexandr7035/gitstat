package com.alexandr7035.gitstat.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.core.Language
import com.alexandr7035.gitstat.core.ProgLangManager
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.data.local.CacheDB
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.data.local.model.UserEntity
import com.alexandr7035.gitstat.data.remote.NetworkModule
import com.alexandr7035.gitstat.data.remote.mappers.RepositoryRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.UserRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.model.RepositoryModel
import com.alexandr7035.gitstat.view.repositories_list.filters.ReposFilters
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val appPreferences: AppPreferences,
    private val application: Application,
    private val api: NetworkModule,
    private val langManager: ProgLangManager,
    private val gson: Gson
) {

    private val LOG_TAG = "DEBUG_TAG"
    private val dao = CacheDB.getInstance(context = application).getDao()
    //private val api = NetworkModule(token)

    private val syncStateLiveData = MutableLiveData<SyncStatus>()

    private val userMapper = UserRemoteToCacheMapper()
    private val repoMapper = RepositoryRemoteToCacheMapper()

    suspend fun getUserLiveDataFromCache(livedata: MutableLiveData<UserEntity>) {

        livedata.postValue(dao.getUserCache())

        // For loading animations, etc
        syncStateLiveData.postValue(SyncStatus.PENDING)

        try {
            val res = api.getUserData()
            ////Log.d(LOG_TAG, "USER cache request")

            if (res.isSuccessful) {

                syncStateLiveData.postValue(SyncStatus.SUCCESS)

                // Map remote model to room
                val cachedUser = userMapper.transform(res.body()!!)
                dao.insertUserCache(cachedUser)

            } else {
                syncStateLiveData.postValue(SyncStatus.FAILED)
            }

        }
        catch (e: Exception) {
                ////Log.d(LOG_TAG, "exception $e")
                syncStateLiveData.postValue(SyncStatus.FAILED)
        }

        livedata.postValue(dao.getUserCache())
    }


    suspend fun getRepositoriesLiveDataFromCache(livedata: MutableLiveData<List<RepositoryEntity>>) {

        livedata.postValue(dao.getRepositoriesCache())

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

        livedata.postValue(dao.getRepositoriesCache())

    }


    fun getSyncStatusLiveData(): MutableLiveData<SyncStatus> {
        return syncStateLiveData
    }


    fun doLoginRequest(loginLiveData: MutableLiveData<Int>, token: String) {

        ////Log.d(LOG_TAG, "repo DO LOGIN REQ")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = api.loginRequest(token)

                withContext(Dispatchers.Main) {
                    ////Log.d(LOG_TAG, "code ${res.code()}")
                    loginLiveData.value = res.code()
                }
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    ////Log.d(LOG_TAG, "code ")
                    loginLiveData.value = 0
                }
            }
        }

    }


    fun clearAllCache() {
        CoroutineScope(Dispatchers.IO).launch {
            dao.clearRepositoriesCache()
            dao.clearUserCache()
        }
    }

    fun getAllRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return dao.getAllRepositoriesLiveData()
    }

   fun getActiveRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return dao.getActiveRepositoriesLiveData()
    }

    fun getArchivedRepositoriesLiveData(): LiveData<List<RepositoryEntity>> {
        return dao.getArchivedRepositoriesLiveData()
    }

    fun saveToken(token: String) {
        appPreferences.token = token
    }

    fun doLogout() {
        appPreferences.token = null
    }

    fun checkIfLoggedIn(): Boolean {
        return when (appPreferences.token) {
            null -> false
            else -> true
        }
    }

    fun getLanguagesForReposList(repos: List<RepositoryEntity>): List<Language> {
        return langManager.getLanguagesList(repos)
    }

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

}