package by.alexandr7035.gitstat.data

import androidx.lifecycle.MutableLiveData
import by.alexandr7035.gitstat.apollo.*
import by.alexandr7035.gitstat.core.*
import by.alexandr7035.gitstat.data.local.CacheDB
import by.alexandr7035.gitstat.data.local.model.*
import by.alexandr7035.gitstat.data.remote.mappers.*
import by.alexandr7035.gitstat.extensions.debug
import by.alexandr7035.gitstat.extensions.performRequestWithDataResult
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

    // Set livedata to non-null when need to observe sync status on UI
    // Null by default
    suspend fun syncData(syncStatusLiveData: MutableLiveData<DataSyncStatus>? = null) {

        try {

            // Get creation and current years
            val profileCreationDate = apolloClient.performRequestWithDataResult(ProfileCreationDateQuery()).viewer.createdAt as String
            val unixCreationDate = timeHelper.getUnixDateFromISO8601(profileCreationDate)
            val accountCreationYear = timeHelper.getYearFromUnixDate(unixCreationDate)
            val currentYear = timeHelper.getYearFromUnixDate(System.currentTimeMillis())

            syncStatusLiveData?.postValue(DataSyncStatus.PENDING_PROFILE)
            val profile = fetchProfile()

            syncStatusLiveData?.postValue(DataSyncStatus.PENDING_REPOSITORIES)
            val repositories = fetchRepositories()

            syncStatusLiveData?.postValue(DataSyncStatus.PENDING_CONTRIBUTIONS)
            val contributionYears = fetchContributionYears(accountCreationYear, currentYear)
            val contributionDays = fetchContributionDays(accountCreationYear, currentYear)
            val contributionTypes = fetchContributionTypes(accountCreationYear, currentYear)
            val contributionRates = fetchContributionRates(contributionDays)

            // Write cache
            clearCache()

            Timber.tag("DEBUG_TAG").d("profile $profile")
            db.getUserDao().insertUser(profile)
            db.getRepositoriesDao().insertRepositories(repositories)

            db.getContributionsDao().apply {
                insertContributionDays(contributionDays)
                insertContributionYearsCache(contributionYears)
                insertContributionsRatios(contributionTypes)
                insertContributionRatesCache(contributionRates)
            }

            appPreferences.lastSuccessCacheSyncDate = System.currentTimeMillis()

            syncStatusLiveData?.postValue(DataSyncStatus.SUCCESS)
        }

        catch (e: AppError) {

            Timber.tag("DEBUG_SYNC").e("Catch exception during data sync ${e.type.name}")

            when (e.type) {
                ErrorType.FAILED_CONNECTION -> {
                    syncStatusLiveData?.postValue(DataSyncStatus.FAILED_NETWORK)
                }

                ErrorType.FAILED_AUTHORIZATION -> {
                    syncStatusLiveData?.postValue(DataSyncStatus.AUTHORIZATION_ERROR)
                }

                ErrorType.UNKNOWN_ERROR -> {
                    // TODO new status
                    syncStatusLiveData?.postValue(DataSyncStatus.FAILED_NETWORK)
                }
            }
        }

    }


    private suspend fun fetchProfile(): UserEntity {
        val data = apolloClient.performRequestWithDataResult(ProfileQuery())
        return profileMapper.transform(data)
    }

    private suspend fun fetchRepositories(): List<RepositoryEntity> {
        val data = apolloClient.performRequestWithDataResult(RepositoriesQuery())
        return repositoriesMapper.transform(data)
    }

    private fun fetchContributionYears(accountCreationYear: Int, currentYear: Int): List<ContributionsYearEntity> {
        val years = ArrayList<ContributionsYearEntity>()

        for (year in accountCreationYear..currentYear) {
            years.add(ContributionsYearEntity(year))
        }

        return years
    }

    private suspend fun fetchContributionDays(profileCreationYear: Int, currentYear: Int): List<ContributionDayEntity> {

        val contributionDaysCached = ArrayList<ContributionDayEntity>()

        // Date range more than a year is not allowed in this api
        // So we have to deal with multiple requests
        for (year in profileCreationYear..currentYear) {

            val contributionsData = getContributionsForDateRange(year)
            // Map apollo result into room cache
            val cachedContributionsData = contributionsMapper.transform(contributionsData)
            contributionDaysCached.addAll(cachedContributionsData)
        }

        return contributionDaysCached
    }


    // TODO try to find better solution later
    private suspend fun fetchContributionTypes(profileCreationYear: Int, currentYear: Int): ArrayList<ContributionsRatioEntity> {

        val contributionsRatioCached = ArrayList<ContributionsRatioEntity>()

        // Date range more than a year is not allowed in this api
        // So we have to deal with multiple requests
        for (year in profileCreationYear..currentYear) {

            // We need to get totalContributions here and pass them to mapper
            // Some of contributions do not belong to 5 groups (commits, issues, PRs, code reviews, repositories)
            // that Github API provides. We mark them as "Unknown" in mapper
            val yearContributions = db.getContributionsDao().getContributionYearWithDays(year)
            val totalContributionsCount = yearContributions.contributionDays.sumOf { it.count }

            val ratioData = getContributionsRatioForDateRange(year)
            contributionsRatioCached.add(ratioMapper.transform(ratioData, totalContributionsCount))
        }

        return contributionsRatioCached
    }


    private fun fetchContributionRates(contributionDays: List<ContributionDayEntity>): List<ContributionRateEntity> {
        return daysToRatesMapper.transform(contributionDays)
    }


    // Pass null to get contributions for the last year
    // FIXME DRY
    private suspend fun getContributionsForDateRange(year: Int?): ContributionsQuery.Data {

        return if (year == null) {
            apolloClient.performRequestWithDataResult(ContributionsQuery(date_from = null, date_to = null))
        }
        else {
            val iso8601Year = timeHelper.getDatesRangeForYear_iso8601(year)
            apolloClient.performRequestWithDataResult(
                ContributionsQuery(
                    date_from = iso8601Year.startDate,
                    date_to = iso8601Year.endDate
                )
            )
        }
    }


    private suspend fun getContributionsRatioForDateRange(year: Int?): ContributionsRatioQuery.Data {

        return if (year == null) {
            apolloClient.performRequestWithDataResult(ContributionsRatioQuery(date_from = null, date_to = null))
        }
        else {
            val iso8601Year = timeHelper.getDatesRangeForYear_iso8601(year)
            apolloClient.performRequestWithDataResult(
                ContributionsRatioQuery(
                    date_from = iso8601Year.startDate,
                    date_to = iso8601Year.endDate
                )
            )
        }
    }

    fun checkIfCacheExists(): Boolean {
        return appPreferences.lastSuccessCacheSyncDate != 0L
    }

    fun checkIfTokenSaved(): Boolean {
        return appPreferences.token != null
    }

    fun clearCache() {
        // Be patient with livedata. This call causes updates with null
        // So wrap code in observers with null check
        db.clearAllTables()
        appPreferences.lastSuccessCacheSyncDate = 0
    }

    fun clearToken() {
        appPreferences.token = null
    }

    fun getLastCacheSyncDateText(): String {
        return timeHelper.getFullFromUnixDate(appPreferences.lastSuccessCacheSyncDate)
    }

}