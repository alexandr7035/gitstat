package com.example.gitstat.data

import android.app.Application
import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gitstat.common.SyncStatus
import com.example.gitstat.data.local.CacheDB
import com.example.gitstat.data.local.UserEntity
import com.example.gitstat.data.remote.NetworkModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class Repository(
    private val application: Application,
    private val user: String,
    private val token: String
) {
    private val LOG_TAG = "DEBUG_TAG"
    private val dao = CacheDB.getInstance(context = application).getDao()
    private val api = NetworkModule(application, user, token)

    private val syncStateLiveData = MutableLiveData<String>()

    fun getUserLiveDataFromCache(user_id: Long): LiveData<UserEntity> {

        //testInsertToCache()

        CoroutineScope(Dispatchers.IO).launch {

            // For loading animations, etc
            syncStateLiveData.postValue(SyncStatus.PENDING)

            val res = api.getUserData()

            Log.d(LOG_TAG, "cache updated ${res.body()}")
            Log.d(LOG_TAG, "" + System.currentTimeMillis())

            if (res.isSuccessful) {

                syncStateLiveData.postValue(SyncStatus.SUCCESS)

                // Convert string dates to timestamp
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                format.timeZone = TimeZone.getTimeZone("GMT")
                val created_date = format.parse(res.body()!!.created_at).time
                val updated_date = format.parse(res.body()!!.updated_at).time

                Log.d(LOG_TAG, DateFormat.format("dd.MM.yyyy HH:mm", created_date).toString())
                Log.d(LOG_TAG, DateFormat.format("dd.MM.yyyy HH:mm", updated_date).toString())

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
                    created_at = created_date,
                    updated_at = updated_date,

                    cache_updated_at = System.currentTimeMillis()
                )

                dao.insertUserCache(cachedUser)

            }
            else {
                syncStateLiveData.postValue(SyncStatus.FAILED)
            }

        }

        return dao.getUserCache(user_id)
    }


    fun getSyncStatusLiveData(): MutableLiveData<String> {
        return syncStateLiveData
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

            created_at = 123,
            updated_at = 12345,

            cache_updated_at = 0
        )

        // FIXME this is a test
        GlobalScope.launch {
            dao.insertUserCache(testUser)
        }
    }
}