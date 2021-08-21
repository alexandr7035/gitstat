package com.alexandr7035.gitstat.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.common.SyncStatus
import com.alexandr7035.gitstat.data.local.CacheDB
import com.alexandr7035.gitstat.data.local.RepositoryEntity
import com.alexandr7035.gitstat.data.local.UserEntity
import com.alexandr7035.gitstat.data.remote.NetworkModule
import com.alexandr7035.gitstat.data.remote.RepositoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun getUserLiveDataFromCache(user: String): LiveData<UserEntity> {

        CoroutineScope(Dispatchers.IO).launch {

            // For loading animations, etc
            syncStateLiveData.postValue(SyncStatus.PENDING)

            try {
                val res = api.getUserData()
                //Log.d(LOG_TAG, "USER cache request")

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

    fun getSyncStatusLiveData(): MutableLiveData<String> {
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

                    val searchResults = res.body()
                    val reposList: List<RepositoryModel> = searchResults!!.items

                    //Log.d(LOG_TAG, "REPOOOOOOOS $reposList")

                    var cachedReposList = ArrayList<RepositoryEntity>()
                    for (repo in reposList) {

                        if (repo.language == null) {
                            repo.language = "Unknown"
                        }

                        val cachedRepo = RepositoryEntity(
                            id = repo.id,
                            name = repo.name,
                            language = repo.language,
                            isPrivate = repo.private
                        )

                        cachedReposList.add(cachedRepo)
                    }

                    dao.clearRepositoriesCache()
                    dao.insertRepositoriesCache(cachedReposList)
                } else {
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