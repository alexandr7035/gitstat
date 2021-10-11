package com.alexandr7035.gitstat.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.apollo.ContributionsQuery
import com.alexandr7035.gitstat.apollo.ProfileCreationDateQuery
import com.alexandr7035.gitstat.apollo.ProfileQuery
import com.alexandr7035.gitstat.apollo.RepositoriesQuery
import com.alexandr7035.gitstat.core.*
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionRateEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYearEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYearWithDays
import com.alexandr7035.gitstat.data.remote.mappers.ContributionsDaysListRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.RepositoriesRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.UserRemoteToCacheMapper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Query
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.math.round

class SyncRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dao: CacheDao,
    private val profileMapper: UserRemoteToCacheMapper,
    private val repositoriesMapper: RepositoriesRemoteToCacheMapper,
    private val contributionsMapper: ContributionsDaysListRemoteToCacheMapper,
    private val timeHelper: TimeHelper,
    private val appPreferences: AppPreferences) {

    fun checkIfTokenSaved(): Boolean {
        return appPreferences.token != null
    }

    fun clearToken() {
        appPreferences.token = null
    }

    suspend fun syncAllData(syncLiveData: MutableLiveData<DataSyncStatus>) {

        val start = System.currentTimeMillis()

        try {
            syncLiveData.postValue(DataSyncStatus.PENDING_PROFILE)
            syncProfileData()

            syncLiveData.postValue(DataSyncStatus.PENDING_REPOSITORIES)
            syncRepositories()

            syncLiveData.postValue(DataSyncStatus.PENDING_CONTRIBUTIONS)
            syncAllContributions()

            // TODO FIXME
            syncContributionRateData()

            syncLiveData.postValue(DataSyncStatus.SUCCESS)

            // Save sync time to preferences if success
            saveSyncTime()

            val end = System.currentTimeMillis()

            Log.d("DEBUG_TAG", "sync finished in ${end-start} ms")
        }
        catch (e: AppError) {
            when (e.type) {
                ErrorType.FAILED_CONNECTION -> {
                    if (checkForCache()) {
                        syncLiveData.postValue(DataSyncStatus.FAILED_NETWORK_WITH_CACHE)
                    }
                    else {
                        syncLiveData.postValue(DataSyncStatus.FAILED_NETWORK_WITH_NO_CACHE)
                    }
                }

                ErrorType.FAILED_AUTHORIZATION -> {
                    syncLiveData.postValue(DataSyncStatus.AUTHORIZATION_ERROR)
                }

                ErrorType.UNKNOWN_ERROR -> {
                    if (checkForCache()) {
                        syncLiveData.postValue(DataSyncStatus.FAILED_NETWORK_WITH_CACHE)
                    }
                    else {
                        syncLiveData.postValue(DataSyncStatus.FAILED_NETWORK_WITH_NO_CACHE)
                    }
                }
            }
        }
    }


    suspend fun clearCache() {
        dao.clearRepositoriesCache()
        dao.clearContributionsDaysCache()
        dao.clearContributionsYearsCache()
        dao.clearUserCache()
    }


    private suspend fun syncAllContributions() {

        val profileCreationDate = performApolloRequest(ProfileCreationDateQuery()).viewer.createdAt as String

        val unixCreationDate = timeHelper.getUnixDateFromISO8601(profileCreationDate)
        val creationYear = timeHelper.getYearFromUnixDate(unixCreationDate)
        val currentYear = timeHelper.getYearFromUnixDate(System.currentTimeMillis())

        Log.d("DEBUG_TAG", "$creationYear $currentYear")

        val contributionDaysCached = ArrayList<ContributionDayEntity>()
        val contributionYears = ArrayList<ContributionsYearEntity>()

        // Date range more than a year is not allowed in this api
        // So we have to deal with multiple requests
        for (year in creationYear..currentYear) {

            contributionYears.add(ContributionsYearEntity(year))

            val resData = getContributionsForDateRange(year)
            // Transform apollo result into room cache
            contributionDaysCached.addAll(contributionsMapper.transform(resData))
        }

        dao.clearContributionsDaysCache()
        dao.insertContributionsDaysCache(contributionDaysCached)
        dao.clearContributionsYearsCache()
        dao.insertContributionYearsCache(contributionYears)
    }


    private suspend fun syncProfileData() {
        val data = performApolloRequest(ProfileQuery())
        val cachedProfile = profileMapper.transform(data)
        dao.clearUserCache()
        dao.insertUserCache(cachedProfile)
    }

    private suspend fun syncRepositories() {
        val data = performApolloRequest(RepositoriesQuery())
        val cachedRepositories = repositoriesMapper.transform(data)
        Log.d("DEBUG_TAG", "${cachedRepositories.size}")
        dao.insertRepositoriesCache(cachedRepositories)
    }


    private suspend fun syncContributionRateData() {

        val contributionDays = dao.getContributionsDaysCacheList()
        val rates = ArrayList<ContributionRateEntity>()

        contributionDays.forEachIndexed { position, day ->
            val daysSlice = contributionDays.slice(0..position)
            val daysSliceContributionsCount = daysSlice.sumOf { it.count }

            val rate = round((daysSliceContributionsCount.toFloat() / daysSlice.size.toFloat() * 100)) / 100F
            Log.d("DEBUG_TAG", "count ${daysSliceContributionsCount.toFloat()} / ${daysSlice.size.toFloat()} * 100 / 100f")

            rates.add(ContributionRateEntity(
                date = day.date,
                rate = rate,
                yearId = day.yearId
            ))
        }

        dao.clearContributionsYearsWithRatesCache()
        dao.insertContributionRatesCache(rates)
    }


    // Pass null to get contributions for the last year
    private suspend fun getContributionsForDateRange(year: Int?): ContributionsQuery.Data {

        return if (year == null) {
            performApolloRequest(ContributionsQuery(date_from = null, date_to = null))
        }
        else {
            val iso8601Year = timeHelper.getDatesRangeForYear_iso8601(year)
            performApolloRequest(
                ContributionsQuery(
                date_from = iso8601Year.startDate,
                date_to = iso8601Year.endDate
            ))
        }
    }


    // Generic request handling
    // Handle network errors here
    private suspend fun <D : Query.Data> performApolloRequest(query: Query<D>): D {
        val response = apolloClient.query(query)

        if (response.hasErrors()) {
            throw AppError(ErrorType.UNKNOWN_ERROR)
        }
        else {
            if (response.data == null) {
                throw AppError(ErrorType.UNKNOWN_ERROR)
            }
            else {
                return response.data!!
            }
        }
    }

    fun getLastCacheSyncDateText(): String {
        return timeHelper.getFullFromUnixDate(appPreferences.lastSuccessCacheSyncDate)
    }

    private fun checkForCache(): Boolean {
        return appPreferences.lastSuccessCacheSyncDate != 0L
    }

    private fun saveSyncTime() {
        // TODO check for timezone
        appPreferences.lastSuccessCacheSyncDate = System.currentTimeMillis()
    }

    private fun clearSyncTime() {
        appPreferences.lastSuccessCacheSyncDate = 0
    }

}