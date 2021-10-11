package com.alexandr7035.gitstat.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexandr7035.gitstat.data.local.model.*

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

    @Query("select * from repositories where not isArchived")
    suspend fun getActiveRepositoriesCache(): List<RepositoryEntity>

    @Query("select * from repositories where isArchived")
    suspend fun getArchivedRepositoriesCache(): List<RepositoryEntity>

    @Query("DELETE FROM repositories")
    suspend fun clearRepositoriesCache()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContributionsDaysCache(contributions: List<ContributionDayEntity>)

    @Query("select * from contributions order by date")
    fun getContributionsDaysCacheLiveData(): LiveData<List<ContributionDayEntity>>

    @Query("select * from contributions order by date")
    suspend fun getContributionsDaysCache(): List<ContributionDayEntity>

    @Query("DELETE FROM contributions")
    suspend fun clearContributionsDaysCache()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContributionYearsCache(years: List<ContributionsYearEntity>)

    @Query("SELECT * FROM contribution_years")
    fun getContributionYearsCache(): LiveData<List<ContributionsYearWithDays>>

    // FIXME
    @Query("SELECT * FROM contribution_years")
    suspend fun getContributionYearsList(): List<ContributionsYearWithDays>

    @Query("DELETE FROM contribution_years")
    fun clearContributionsYearsCache()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContributionRatesCache(rates: ArrayList<ContributionRateEntity>)

    @Query("SELECT * FROM contribution_years")
    fun getContributionYearsWithRatesCache(): LiveData<List<ContributionsYearWithRates>>

    @Query("DELETE FROM contribution_rates")
    suspend fun clearContributionsYearsWithRatesCache()
}