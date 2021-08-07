package com.example.gitstat.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.gitstat.data.local.CacheDB
import com.example.gitstat.data.local.UserEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(private val context: Context) {

    private val db: CacheDB = CacheDB.getInstance(context = this.context)
    private val dao = db.getDao()
    private val LOG_TAG = "DEBUG_TAG"

    fun getUserLiveDataFromCache(user_id: Long): LiveData<UserEntity> {
        Log.d(LOG_TAG, "our DB test")

        val testUser = UserEntity(
            id = 22574399,
            login = "alexandr7035",
            avatar_url = "https://avatars.githubusercontent.com/u/22574399?v=4",
            name = "Alexandr Alexeenko",
            location = "Ukraine",
            public_repos = 30,
            total_private_repos = 10,

            followers = 10,
            following = 10,

            created_at = "06.01.2017",
            updated_at = "06.01.2017",

            cache_updated_at = 0
        )

        // FIXME this is a test
        GlobalScope.launch {
            dao.insertUserCache(testUser)
        }

        return dao.getUserCache(user_id)
    }

    fun updateUserLiveData(user_id: Long) {

    }

}