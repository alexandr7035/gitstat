package com.example.gitstat.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gitstat.common.SyncStatus
import com.example.gitstat.data.local.CacheDB
import com.example.gitstat.data.local.RepositoryEntity
import com.example.gitstat.data.local.UserEntity
import com.example.gitstat.data.remote.NetworkModule
import com.example.gitstat.data.remote.RepositoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Repository(
    private val application: Application,
    private val user: String,
    private val token: String
) {
    private val LOG_TAG = "DEBUG_TAG"
    private val dao = CacheDB.getInstance(context = application).getDao()
    private val api = NetworkModule(application, user, token)

    private val syncStateLiveData = MutableLiveData<String>()

    private lateinit var languagesList: TreeMap<String, Int>

    fun getUserLiveDataFromCache(user_id: Long): LiveData<UserEntity> {

        CoroutineScope(Dispatchers.IO).launch {

            // For loading animations, etc
            syncStateLiveData.postValue(SyncStatus.PENDING)

            val res = api.getUserData()
            Log.d(LOG_TAG, "USER cache request")

            if (res.isSuccessful) {

                syncStateLiveData.postValue(SyncStatus.SUCCESS)

                // Convert string dates to timestamp
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                format.timeZone = TimeZone.getTimeZone("GMT")
                val created_date = format.parse(res.body()!!.created_at).time
                val updated_date = format.parse(res.body()!!.updated_at).time

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

    fun getRepositoriesLiveDataFromCache(): LiveData<List<RepositoryEntity>> {

        Log.d(LOG_TAG, "REPOS CACHE 2")

        CoroutineScope(Dispatchers.IO).launch {
            val res = api.getRepositoriesData()

            if (res.isSuccessful) {

                val searchResults = res.body()
                val reposList: List<RepositoryModel> = searchResults!!.items

                Log.d(LOG_TAG, "REPOOOOOOOS $reposList")

                var cachedReposList = ArrayList<RepositoryEntity>()
                for (repo in reposList) {

                    if (repo.language == null) {
                        repo.language = "Unknown"
                    }

                    val cachedRepo = RepositoryEntity(
                        id = repo.id,
                        name = repo.name,
                        language = repo.language,
                        isPrivate = repo.private)

                    cachedReposList.add(cachedRepo)
                }

                dao.insertRepositoriesCache(cachedReposList)
            }
        }

        return dao.getRepositoriesCache()

    }

}