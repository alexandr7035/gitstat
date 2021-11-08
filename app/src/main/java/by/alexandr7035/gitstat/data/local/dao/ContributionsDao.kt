package by.alexandr7035.gitstat.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.alexandr7035.gitstat.data.local.model.*

@Dao
interface ContributionsDao {

    /////////////////////////////////////
    // Contribution days
    /////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContributionDays(contributions: List<ContributionDayEntity>)

    @Query("select * from contributions order by date")
    fun getContributionDaysLiveData(): LiveData<List<ContributionDayEntity>>

    @Query("select * from contributions order by date asc")
    fun getContributionDaysList(): List<ContributionDayEntity>

    @Query("DELETE FROM contributions")
    suspend fun clearContributionDays()

    /////////////////////////////////////
    // Contribution years
    /////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContributionYearsCache(years: List<ContributionsYearEntity>)


    @Query("SELECT * FROM contribution_years")
    fun getContributionYearsWithDaysLiveData(): LiveData<List<ContributionsYearWithDays>>

    @Query("SELECT * FROM contribution_years where id = (:yearId)")
    suspend fun getContributionYearWithDays(yearId: Int): ContributionsYearWithDays

    @Query("DELETE FROM contribution_years")
    fun clearContributionYears()

    /////////////////////////////////////
    // Contribution rates
    /////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContributionRatesCache(rates: List<ContributionRateEntity>)

    @Query("SELECT * FROM contribution_years")
    fun getContributionYearsWithRatesCache(): LiveData<List<ContributionsYearWithRates>>

    @Query("DELETE FROM contribution_rates")
    suspend fun clearContributionsYearsWithRatesCache()

    @Query("SELECT * FROM contribution_rates")
    suspend fun getContributionYearWithRates(): ContributionsYearWithRates

    /////////////////////////////////////
    // Contribution ratio (issues, created repos, PRs, commits, code reviews, unknown)
    /////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContributionsRatios(ratioData: List<ContributionsRatioEntity>)

    @Query("select * from contributions_ratio")
    fun getContributionRatiosLiveData(): LiveData<List<ContributionsRatioEntity>>

    @Query("DELETE FROM contributions_ratio")
    suspend fun clearContributionsRatios()
}