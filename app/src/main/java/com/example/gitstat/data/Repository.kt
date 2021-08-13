package com.example.gitstat.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.gitstat.data.local.CacheDB
import com.example.gitstat.data.local.UserEntity
import com.example.gitstat.data.remote.NetworkModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(
    private val application: Application,
    private val user: String,
    private val token: String
) {

    private val db: CacheDB = CacheDB.getInstance(context = application)
    private val dao = db.getDao()
    private val LOG_TAG = "DEBUG_TAG"
    private val api = NetworkModule(application, user, token)

    init {



    }


    fun getUserLiveDataFromCache(user_id: Long): LiveData<UserEntity> {

        //testInsertToCache()

        CoroutineScope(Dispatchers.IO).launch {
            val res = api.getUserData()

            Log.d(LOG_TAG, "cache updated ${res.body()}")
            Log.d(LOG_TAG, "" + System.currentTimeMillis())

            if (res.isSuccessful) {
                val cachedUser = UserEntity(
                    id = res.body()!!.id,
                    name = res.body()!!.name,
                    location = res.body()!!.location,
                    login = res.body()!!.login,
                    avatar_url = res.body()!!.avatar_url,
                    public_repos = res.body()!!.public_repos,
                    total_private_repos = res.body()!!.total_private_repos,
                    followers = res.body()!!.followers,
                    following = res.body()!!.following,
                    created_at = res.body()!!.created_at,
                    updated_at = res.body()!!.updated_at,

                    cache_updated_at = System.currentTimeMillis()
                )

                dao.updateUserCache(cachedUser)

            }

        }

        return dao.getUserCache(user_id)
    }

    fun updateUserLiveData(user_id: Long) {

    }

    private fun testInsertToCache() {
        Log.d(LOG_TAG, "our DB test")

        val testUser = UserEntity(
            id = 22574399,
            login = "alexandr7035",
            avatar_url = "https://avatars.githubusercontent.com/u/22574399?v=4",
            name = "Alexandr Alexeenko",
            location = "Belarus",
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
    }
}