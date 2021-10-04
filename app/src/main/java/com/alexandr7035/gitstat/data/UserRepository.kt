package com.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: CacheDao) {

    fun getUserLiveDataFromCache(): LiveData<UserEntity> {
        return dao.getUserCacheLiveData()
    }

}