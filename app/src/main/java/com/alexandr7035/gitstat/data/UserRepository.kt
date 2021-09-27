package com.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.UserEntity
import com.alexandr7035.gitstat.data.remote.RestApiHelper
import com.alexandr7035.gitstat.data.remote.mappers.UserRemoteToCacheMapper
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: RestApiHelper,
    private val dao: CacheDao,
    private val userMapper: UserRemoteToCacheMapper) {

    private val syncStateLiveData = MutableLiveData<SyncStatus>()

    fun getUserLiveDataFromCache(): LiveData<UserEntity> {
        return dao.getUserCacheLiveData()
    }

    suspend fun syncUserData() {
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
            syncStateLiveData.postValue(SyncStatus.FAILED)
        }

    }

    // TODO find better solution to pass status
    fun getSyncStatusLiveData(): MutableLiveData<SyncStatus> {
        return syncStateLiveData
    }

}