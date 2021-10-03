package com.alexandr7035.gitstat.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.apollo.ContributionsQuery
import com.alexandr7035.gitstat.apollo.ProfileCreationDateQuery
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.remote.mappers.ContributionDayRemoteToCacheMapper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

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


//        val response = apolloClient.query(ContributionsQuery("", ""))

        // Date range more than a year is not allowed in this api

        var isFetchingSuccessFull = true

        var creationYear: Int = 0

        val profileCreationDateRes = getProfileCreationDate()
        if (profileCreationDateRes.hasErrors()) {
            Log.e("DEBUG_APOLLO", "${profileCreationDateRes.errors}")
            isFetchingSuccessFull = false
        }
        else {
            val date = profileCreationDateRes.data?.viewer?.createdAt as String

            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            format.timeZone = TimeZone.getTimeZone("GMT")
            val unixDate = format.parse(date)!!.time

            val yearFormat = SimpleDateFormat("yyyy", Locale.US)
            yearFormat.timeZone = TimeZone.getTimeZone("GMT")

            creationYear = yearFormat.format(unixDate).toInt()
        }

        val yearFormat = SimpleDateFormat("yyyy", Locale.US)
        yearFormat.timeZone = TimeZone.getTimeZone("GMT")
        val currentYear = yearFormat.format(System.currentTimeMillis()).toInt()

        Log.d("DEBUG_TAG", "$creationYear $currentYear")

        val contributionDays = ArrayList<ContributionsQuery.ContributionDay>()

        for (year in creationYear..currentYear) {
            val response = getContributionsForDateRange(year = year)

            if (response.hasErrors()) {
                Log.e("DEBUG_APOLLO", "${response.errors}")
                isFetchingSuccessFull = false
            }

            else {
                if (response.data?.viewer?.contributionsCollection?.contributionCalendar?.weeks != null) {
                    for (week in response.data!!.viewer.contributionsCollection.contributionCalendar.weeks) {
                        for (day in week.contributionDays) {
                            Log.d("DEBUG_APOLLO", "$day")
                            contributionDays.add(day)
                        }
                    }
                }
            }
        }


        if (isFetchingSuccessFull) {
            val cachedContributions = contributionDays.map { remoteDay ->
                mapper.transform(remoteDay)
            }

            dao.clearLastYearContributionsDaysCache()
            dao.insertLastYearContributionsDaysCache(cachedContributions)
        }


//        val response = getContributionsForDateRange(year = 2021)
//
//        // FIXME error handling
//        if (response.hasErrors()) {
//            Log.d("DEBUG_APOLLO", "errors")
//            Log.e("DEBUG_APOLLO", "${response.errors}")
//        }
//        else {
//            Log.d("DEBUG_APOLLO", "success")
//            Log.d("DEBUG_APOLLO", "data ${ response.data?.viewer?.contributionsCollection}")
//
//            if (response.data?.viewer?.contributionsCollection?.contributionCalendar?.weeks != null) {
//                for (week in response.data!!.viewer.contributionsCollection.contributionCalendar.weeks) {
//                    for (day in week.contributionDays) {
//                        Log.d("DEBUG_APOLLO", "$day")
//                        contributionDays.add(day)
//                    }
//                }
//
//                Log.d("DEBUG_APOLLO", "contributions $contributionDays")
//
//                val cachedContributions = contributionDays.map { remoteDay ->
//                    mapper.transform(remoteDay)
//                }
//
//                dao.clearLastYearContributionsDaysCache()
//                dao.insertLastYearContributionsDaysCache(cachedContributions)
//            }
//        }
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


    private suspend fun getProfileCreationDate(): ApolloResponse<ProfileCreationDateQuery.Data> {
        return apolloClient.query(ProfileCreationDateQuery())
    }
}