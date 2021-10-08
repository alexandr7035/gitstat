package com.alexandr7035.gitstat.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.apollo.ContributionsQuery
import com.alexandr7035.gitstat.apollo.ProfileCreationDateQuery
import com.alexandr7035.gitstat.apollo.ProfileQuery
import com.alexandr7035.gitstat.apollo.RepositoriesQuery
import com.alexandr7035.gitstat.core.AppError
import com.alexandr7035.gitstat.core.DataSyncStatus
import com.alexandr7035.gitstat.core.ErrorType
import com.alexandr7035.gitstat.core.TimeHelper
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.remote.mappers.ContributionsDaysListRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.RepositoriesRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.UserRemoteToCacheMapper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Query
import javax.inject.Inject

class SyncRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dao: CacheDao,
    private val profileMapper: UserRemoteToCacheMapper,
    private val repositoriesMapper: RepositoriesRemoteToCacheMapper,
    private val contributionsMapper: ContributionsDaysListRemoteToCacheMapper,
    private val timeHelper: TimeHelper) {

    suspend fun syncAllData(syncLiveData: MutableLiveData<DataSyncStatus>) {

        val start = System.currentTimeMillis()

        try {
            syncLiveData.postValue(DataSyncStatus.PENDING_PROFILE)
            syncProfileData()

            syncLiveData.postValue(DataSyncStatus.PENDING_REPOSITORIES)
            syncRepositories()

            syncLiveData.postValue(DataSyncStatus.PENDING_CONTRIBUTIONS)
            syncAllContributions()

            syncLiveData.postValue(DataSyncStatus.SUCCESS)

            val end = System.currentTimeMillis()

            Log.d("DEBUG_TAG", "sync finished in ${end-start} ms")
        }
        catch (e: AppError) {
            when (e.type) {
                ErrorType.FAILED_CONNECTION -> {
                    // FIXME check cache
                    syncLiveData.postValue(DataSyncStatus.FAILED_NETWORK_WITH_CACHE)
                }

                ErrorType.FAILED_AUTHORIZATION -> {
                    syncLiveData.postValue(DataSyncStatus.AUTHORIZATION_ERROR)
                }

                ErrorType.UNKNOWN_ERROR -> {
                    // FIXME check cache
                    syncLiveData.postValue(DataSyncStatus.FAILED_NETWORK_WITH_CACHE)
                }
            }
        }
    }


    private suspend fun syncAllContributions() {

        val profileCreationDate = performApolloRequest(ProfileCreationDateQuery()).viewer.createdAt as String

        val unixCreationDate = timeHelper.getUnixDateFromISO8601(profileCreationDate)
        val creationYear = timeHelper.getYearFromUnixDate(unixCreationDate)
        val currentYear = timeHelper.getYearFromUnixDate(System.currentTimeMillis())

        Log.d("DEBUG_TAG", "$creationYear $currentYear")

        val contributionDaysCached = ArrayList<ContributionDayEntity>()

        // Date range more than a year is not allowed in this api
        // So we have to deal with multiple requests
        for (year in creationYear..currentYear) {
            val resData = getContributionsForDateRange(year)
            // Transform apollo result into room cache
            contributionDaysCached.addAll(contributionsMapper.transform(resData))
        }

        dao.clearContributionsDaysCache()
        dao.insertContributionsDaysCache(contributionDaysCached)

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

}