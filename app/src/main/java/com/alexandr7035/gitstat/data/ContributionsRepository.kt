package com.alexandr7035.gitstat.data

import com.alexandr7035.gitstat.data.remote.ApolloHelperImpl
import javax.inject.Inject

// FIXME use interface
class ContributionsRepository @Inject constructor(private val apolloHelper: ApolloHelperImpl) {
    suspend fun test() {
        apolloHelper.test()
    }

    suspend fun getLastYearContributions() {
        apolloHelper.getLastYearContributions()
    }
}