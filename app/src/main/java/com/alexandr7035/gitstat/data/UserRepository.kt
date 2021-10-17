package com.alexandr7035.gitstat.data

import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.data.local.dao.UserDao
import com.alexandr7035.gitstat.data.local.model.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UserDao
) {

    fun getUserLiveDataFromCache(): LiveData<UserEntity> {
        return dao.getUserLiveData()
    }

}