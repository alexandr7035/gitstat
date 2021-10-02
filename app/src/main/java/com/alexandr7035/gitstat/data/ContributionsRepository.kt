package com.alexandr7035.gitstat.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.apollo.ContributionsLastYearQuery
import com.alexandr7035.gitstat.apollo.ContributionsQuery
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.remote.mappers.ContributionDayRemoteToCacheMapper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

// FIXME use interface
class ContributionsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dao: CacheDao,
    private val mapper: ContributionDayRemoteToCacheMapper) {

    // FIXME test
    init {
        GlobalScope.launch {
            syncAllContributions()
        }
    }

    suspend fun syncLastYearContributions() {
//        val contributionDays = ArrayList<ContributionsLastYearQuery.ContributionDay>()
//
//        val response = apolloClient.query(ContributionsLastYearQuery())
//
//        // FIXME error handling
//        if (response.hasErrors()) {
//            Log.d("DEBUG_APOLLO", "errors")
//        }
//        else {
//            Log.d("DEBUG_APOLLO", "success")
//            Log.d("DEBUG_APOLLO", "data ${ response.data?.viewer?.contributionsCollection}")
//
//            if (response.data?.viewer?.contributionsCollection?.contributionCalendar?.weeks != null) {
//                for (week in response.data!!.viewer.contributionsCollection.contributionCalendar.weeks) {
//                    for (day in week.contributionDays) {
////                        Log.d("DEBUG_APOLLO", "$day")
//                        contributionDays.add(day)
//                    }
//                }
//
//                Log.d("DEBUG_APOLLO", "contributions $contributionDays")
//                Log.d("DEBUG_APOLLO", "TOTAL contributions ${contributionDays.size}")
//
//                val cachedContributions = contributionDays.map { remoteDay ->
//                    mapper.transform(remoteDay)
//                }
//
//                Log.d("DEBUG_APOLLO", "TOTAL contributions ${cachedContributions.size}")
//
//                dao.clearLastYearContributionsDaysCache()
//                dao.insertLastYearContributionsDaysCache(cachedContributions)
//            }
//        }
    }

    fun getLastYearContributions(): LiveData<List<ContributionDayEntity>> {
        return dao.getLastYearContributionDaysCache()
    }


    suspend fun syncAllContributions() {
        val contributionDays = ArrayList<ContributionsQuery.ContributionDay>()

//        val response = apolloClient.query(ContributionsQuery("", ""))
        val response = getContributionsForDateRange(year = 2021)

        // FIXME error handling
        if (response.hasErrors()) {
            Log.d("DEBUG_APOLLO", "errors")
            Log.d("DEBUG_APOLLO", "${response.errors}")
        }
        else {
            Log.d("DEBUG_APOLLO", "success")
            Log.d("DEBUG_APOLLO", "data ${ response.data?.viewer?.contributionsCollection}")

            if (response.data?.viewer?.contributionsCollection?.contributionCalendar?.weeks != null) {
                for (week in response.data!!.viewer.contributionsCollection.contributionCalendar.weeks) {
                    for (day in week.contributionDays) {
                        Log.d("DEBUG_APOLLO", "$day")
                        contributionDays.add(day)
                    }
                }

                Log.d("DEBUG_APOLLO", "contributions $contributionDays")

                val cachedContributions = contributionDays.map { remoteDay ->
                    mapper.transform(remoteDay)
                }

                dao.clearLastYearContributionsDaysCache()
                dao.insertLastYearContributionsDaysCache(cachedContributions)
            }
        }
    }

    fun getAllContributions(): LiveData<List<ContributionDayEntity>> {
        return dao.getLastYearContributionDaysCache()
    }


    // Pass null to get contributions for the last year
    private suspend fun getContributionsForDateRange(year: Int?): ApolloResponse<ContributionsQuery.Data> {

        // Format : ISO-8601
        val startDate = "$year-01-01T00:00:00Z"
        val endDate = "$year-12-31T23:59:59Z"

        return if (year == null) {
            apolloClient.query(ContributionsQuery(date_from = null, date_to = null))
        } else {
            apolloClient.query(ContributionsQuery(date_from = startDate, date_to = endDate))
        }
    }
}