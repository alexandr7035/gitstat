package com.alexandr7035.gitstat.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.data.local.model.UserEntity

@Dao
interface CacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCache(user: UserEntity)

    @Query("select * from user where login = (:user)")
    fun getUserCache(user: String): LiveData<UserEntity>

    @Query("DELETE FROM user")
    suspend fun clearUserCache()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositoriesCache(repos: List<RepositoryEntity>)

    @Query("select * from repositories")
    fun getRepositoriesCache(): LiveData<List<RepositoryEntity>>

    @Query("DELETE FROM repositories")
    suspend fun clearRepositoriesCache()
}