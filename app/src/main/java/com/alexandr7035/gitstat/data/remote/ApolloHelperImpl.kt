package com.alexandr7035.gitstat.data.remote

import android.util.Log
import com.alexandr7035.gitstat.apollo.ContributionsLastYearQuery
import com.alexandr7035.gitstat.apollo.TestQuery
import com.alexandr7035.gitstat.apollo.adapter.ContributionsLastYearQuery_ResponseAdapter
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import javax.inject.Inject

// TODO interface
class ApolloHelperImpl @Inject constructor(private val apolloClient: ApolloClient) {
    suspend fun test() {
        val response = apolloClient.query(TestQuery())
        Log.d("DEBUG_APOLLO", "$response")

        if (response.hasErrors()) {
            Log.d("DEBUG_APOLLO", "errors")
        }
        else {
            Log.d("DEBUG_APOLLO", "success")
            Log.d("DEBUG_APOLLO", "data ${ response.data?.viewer?.login}")
        }
    }


    suspend fun syncContributions() {

        val contributionDays = ArrayList<ContributionsLastYearQuery.ContributionDay>()

        val response = apolloClient.query(ContributionsLastYearQuery())

        if (response.hasErrors()) {
            Log.d("DEBUG_APOLLO", "errors")
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
            }
            
        }

    }
}