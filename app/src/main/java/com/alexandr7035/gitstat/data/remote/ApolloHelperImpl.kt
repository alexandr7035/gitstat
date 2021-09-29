package com.alexandr7035.gitstat.data.remote

import android.util.Log
import com.alexandr7035.gitstat.apollo.TestQuery
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
}