package com.alexandr7035.gitstat.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.data.local.model.UserEntity

@Dao
interface CacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCache(user: UserEntity)

    @Query("select * from user")
    fun getUserCache(): UserEntity

    @Query("DELETE FROM user")
    suspend fun clearUserCache()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositoriesCache(repos: List<RepositoryEntity>)

    @Query("select * from repositories")
    fun getRepositoriesCache(): List<RepositoryEntity>

    @Query("DELETE FROM repositories")
    suspend fun clearRepositoriesCache()

    @Query("select * from repositories where fork")
    fun getActiveRepositoriesLiveData(): LiveData<List<RepositoryEntity>>
}