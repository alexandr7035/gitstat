package com.alexandr7035.gitstat.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.data.local.model.UserEntity

@Dao
interface CacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCache(user: UserEntity)

    @Query("select * from user")
    fun getUserCache(): UserEntity

    @Query("select * from user")
    fun getUserCacheLiveData(): LiveData<UserEntity>

    @Query("DELETE FROM user")
    suspend fun clearUserCache()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositoriesCache(repos: List<RepositoryEntity>)

    @Query("select * from repositories")
    suspend fun getRepositoriesCache(): List<RepositoryEntity>

    @Query("select * from repositories where not archived")
    suspend fun getActiveRepositoriesCache(): List<RepositoryEntity>

    @Query("select * from repositories where archived")
    suspend fun getArchivedRepositoriesCache(): List<RepositoryEntity>

    @Query("DELETE FROM repositories")
    suspend fun clearRepositoriesCache()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContributionsDaysCache(contributions: List<ContributionDayEntity>)

    @Query("select * from contributions order by date")
    fun getContributionsDaysCache(): LiveData<List<ContributionDayEntity>>

    @Query("DELETE FROM contributions")
    suspend fun clearContributionsDaysCache()
}