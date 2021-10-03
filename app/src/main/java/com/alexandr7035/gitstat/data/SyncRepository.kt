package com.alexandr7035.gitstat.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.apollo.ContributionsQuery
import com.alexandr7035.gitstat.apollo.ProfileCreationDateQuery
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.core.TimeHelper
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.remote.mappers.ContributionsDaysListRemoteToCacheMapper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Query
import javax.inject.Inject

class SyncRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dao: CacheDao,
    private val mapper: ContributionsDaysListRemoteToCacheMapper,
    private val timeHelper: TimeHelper) {

    suspend fun syncAllContributions(syncLiveData: MutableLiveData<SyncStatus>) {

        try {

            syncLiveData.postValue(SyncStatus.PENDING)

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
                contributionDaysCached.addAll(mapper.transform(resData))
            }

            dao.clearContributionsDaysCache()
            dao.insertContributionsDaysCache(contributionDaysCached)

            syncLiveData.postValue(SyncStatus.SUCCESS)
        }

        // TODO pass errors to UI here
        catch (e: SyncFailedException) {
            Log.d("DEBUG_TAG", "apollo error")
            e.printStackTrace()
            syncLiveData.postValue(SyncStatus.FAILED)
        }

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
            throw SyncFailedException("Apollo sync failed: ERRORS ${response.errors}")
        }
        else {
            if (response.data == null) {
                throw SyncFailedException("Apollo: sync failed: DATA is null")
            }
            else {
                return response.data!!
            }
        }
    }

    class SyncFailedException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
        constructor(cause: Throwable) : this(null, cause)
    }

}