package com.alexandr7035.gitstat.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.data.local.CacheDB
import com.alexandr7035.gitstat.data.local.RepositoryEntity
import com.alexandr7035.gitstat.data.local.UserEntity
import com.alexandr7035.gitstat.data.remote.NetworkModule
import com.alexandr7035.gitstat.data.remote.RepositoryModel
import com.alexandr7035.gitstat.data.remote.mappers.RepositoryRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.UserRemoteToCacheMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository(
    application: Application,
    user: String,
    token: String
) {
    private val LOG_TAG = "DEBUG_TAG"
    private val dao = CacheDB.getInstance(context = application).getDao()
    private val api = NetworkModule(application, user, token)

    private val syncStateLiveData = MutableLiveData<SyncStatus>()

    private val userMapper = UserRemoteToCacheMapper()
    private val repoMapper = RepositoryRemoteToCacheMapper()

    fun getUserLiveDataFromCache(user: String): LiveData<UserEntity> {

        CoroutineScope(Dispatchers.IO).launch {

            // For loading animations, etc
            syncStateLiveData.postValue(SyncStatus.PENDING)

            try {
                val res = api.getUserData()
                //Log.d(LOG_TAG, "USER cache request")

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
                //Log.d(LOG_TAG, "exception $e")
                syncStateLiveData.postValue(SyncStatus.FAILED)
            }
        }

        return dao.getUserCache(user)
    }

    fun getSyncStatusLiveData(): MutableLiveData<SyncStatus> {
        return syncStateLiveData
    }

    fun getRepositoriesLiveDataFromCache(): LiveData<List<RepositoryEntity>> {

        //Log.d(LOG_TAG, "REPOS CACHE 2")

        CoroutineScope(Dispatchers.IO).launch {

            syncStateLiveData.postValue(SyncStatus.PENDING)

            try {
                val res = api.getRepositoriesData()

                if (res.isSuccessful) {

                    syncStateLiveData.postValue(SyncStatus.SUCCESS)

                    val reposList: List<RepositoryModel> = res.body()!!.items

                    val cachedReposList = reposList.map { repositoryModel ->
                        repoMapper.transform(repositoryModel)
                    }

                    dao.clearRepositoriesCache()
                    dao.insertRepositoriesCache(cachedReposList)
                }

                else {
                    syncStateLiveData.postValue(SyncStatus.FAILED)
                }

            }
            catch (e: Exception) {
                //Log.d(LOG_TAG, "exception ${e}")
                syncStateLiveData.postValue(SyncStatus.FAILED)
            }
        }

        return dao.getRepositoriesCache()

    }


    fun doLoginRequest(loginLiveData: MutableLiveData<Int>, user: String, token: String) {

        //Log.d(LOG_TAG, "repo DO LOGIN REQ")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = api.loginRequest(user, token)

                withContext(Dispatchers.Main) {
                    //Log.d(LOG_TAG, "code ${res.code()}")
                    loginLiveData.value = res.code()
                }
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    //Log.d(LOG_TAG, "code ")
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

}