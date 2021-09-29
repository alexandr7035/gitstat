package com.alexandr7035.gitstat.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.alexandr7035.gitstat.apollo.ContributionsLastYearQuery
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.remote.mappers.ContributionDayRemoteToCacheMapper
import com.apollographql.apollo3.ApolloClient
import javax.inject.Inject

// FIXME use interface
class ContributionsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dao: CacheDao,
    private val mapper: ContributionDayRemoteToCacheMapper) {

    suspend fun test() {

    }

    suspend fun syncContributions() {
        val contributionDays = ArrayList<ContributionsLastYearQuery.ContributionDay>()

        val response = apolloClient.query(ContributionsLastYearQuery())

        // FIXME error handling
        if (response.hasErrors()) {
            Log.d("DEBUG_APOLLO", "errors")
        }
        else {
            Log.d("DEBUG_APOLLO", "success")
            Log.d("DEBUG_APOLLO", "data ${ response.data?.viewer?.contributionsCollection}")

            if (response.data?.viewer?.contributionsCollection?.contributionCalendar?.weeks != null) {
                for (week in response.data!!.viewer.contributionsCollection.contributionCalendar.weeks) {
                    for (day in week.contributionDays) {
//                        Log.d("DEBUG_APOLLO", "$day")
                        contributionDays.add(day)
                    }
                }

                Log.d("DEBUG_APOLLO", "contributions $contributionDays")
                Log.d("DEBUG_APOLLO", "TOTAL contributions ${contributionDays.size}")

                val cachedContributions = contributionDays.map { remoteDay ->
                    mapper.transform(remoteDay)
                }

                Log.d("DEBUG_APOLLO", "TOTAL contributions ${cachedContributions.size}")

                dao.clearContributionsDaysCache()
                dao.insertContributionsDaysCache(cachedContributions)
            }
        }
    }

    fun getContributions(): LiveData<List<ContributionDayEntity>> {
        return dao.getContributionDaysCache()
    }
}