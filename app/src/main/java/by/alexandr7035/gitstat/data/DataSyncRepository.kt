package by.alexandr7035.gitstat.data

import by.alexandr7035.gitstat.core.AppPreferences
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.CacheDB
import by.alexandr7035.gitstat.data.remote.mappers.*
import com.apollographql.apollo3.ApolloClient
import timber.log.Timber
import javax.inject.Inject

class DataSyncRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val db: CacheDB,
    private val timeHelper: TimeHelper,
    private val appPreferences: AppPreferences,

    private val profileMapper: UserRemoteToCacheMapper,
    private val repositoriesMapper: RepositoriesRemoteToCacheMapper,
    private val contributionsMapper: ContributionsDaysListRemoteToCacheMapper,
    private val ratioMapper: ContributionsRatioRemoteToCacheMapper,
    private val daysToRatesMapper: ContributionDaysToRatesMapper,
    )
{
    suspend fun test() {
        Timber.tag("DEBUG_SERVICE").d("service repo test")
    }

}