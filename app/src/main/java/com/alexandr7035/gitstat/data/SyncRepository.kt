package com.alexandr7035.gitstat.data

import androidx.lifecycle.MutableLiveData
import com.alexandr7035.gitstat.apollo.*
import com.alexandr7035.gitstat.core.*
import com.alexandr7035.gitstat.data.local.dao.ContributionsDao
import com.alexandr7035.gitstat.data.local.dao.RepositoriesDao
import com.alexandr7035.gitstat.data.local.dao.UserDao
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.ContributionRateEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsRatioEntity
import com.alexandr7035.gitstat.data.local.model.ContributionsYearEntity
import com.alexandr7035.gitstat.data.remote.mappers.ContributionsDaysListRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.ContributionsRatioRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.RepositoriesRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.UserRemoteToCacheMapper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Query
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.round

class SyncRepository @Inject constructor(
    private val apolloClient: ApolloClient,

    private val userDao: UserDao,
    private val reposDao: RepositoriesDao,
    private val contributionsDao: ContributionsDao,

    private val profileMapper: UserRemoteToCacheMapper,
    private val repositoriesMapper: RepositoriesRemoteToCacheMapper,
    private val contributionsMapper: ContributionsDaysListRemoteToCacheMapper,
    private val ratioMapper: ContributionsRatioRemoteToCacheMapper,

    private val timeHelper: TimeHelper,
    private val appPreferences: AppPreferences) {

    fun checkIfTokenSaved(): Boolean {
        return appPreferences.token != null
    }

    fun clearToken() {
        appPreferences.token = null
    }

    suspend fun syncAllData(syncLiveData: MutableLiveData<DataSyncStatus>) {

        val start = System.currentTimeMillis()

        try {
            syncLiveData.postValue(DataSyncStatus.PENDING_PROFILE)
            syncProfileData()

            syncLiveData.postValue(DataSyncStatus.PENDING_REPOSITORIES)
            syncRepositories()

            syncLiveData.postValue(DataSyncStatus.PENDING_CONTRIBUTIONS)
            syncAllContributions()
            syncContributionRateData()

            syncLiveData.postValue(DataSyncStatus.SUCCESS)

            // Save sync time to preferences if success
            saveSyncTime()

            val end = System.currentTimeMillis()

            Timber.d("sync finished in ${end-start} ms")
        }
        catch (e: AppError) {
            when (e.type) {
                ErrorType.FAILED_CONNECTION -> {
                    if (checkForCache()) {
                        syncLiveData.postValue(DataSyncStatus.FAILED_NETWORK_WITH_CACHE)
                    }
                    else {
                        syncLiveData.postValue(DataSyncStatus.FAILED_NETWORK_WITH_NO_CACHE)
                    }
                }

                ErrorType.FAILED_AUTHORIZATION -> {
                    syncLiveData.postValue(DataSyncStatus.AUTHORIZATION_ERROR)
                }

                ErrorType.UNKNOWN_ERROR -> {
                    if (checkForCache()) {
                        syncLiveData.postValue(DataSyncStatus.FAILED_NETWORK_WITH_CACHE)
                    }
                    else {
                        syncLiveData.postValue(DataSyncStatus.FAILED_NETWORK_WITH_NO_CACHE)
                    }
                }
            }
        }
    }


    suspend fun clearCache() {
        reposDao.clearRepositories()
        contributionsDao.clearContributionDays()
        contributionsDao.clearContributionYears()
        userDao.clearUser()
        appPreferences.lastSuccessCacheSyncDate = 0
    }


    private suspend fun syncAllContributions() {

        val profileCreationDate = performApolloRequest(ProfileCreationDateQuery()).viewer.createdAt as String

        val unixCreationDate = timeHelper.getUnixDateFromISO8601(profileCreationDate)
        val creationYear = timeHelper.getYearFromUnixDate(unixCreationDate)
        val currentYear = timeHelper.getYearFromUnixDate(System.currentTimeMillis())

        Timber.d("$creationYear $currentYear")

        val contributionDaysCached = ArrayList<ContributionDayEntity>()
        val contributionsRatioCached = ArrayList<ContributionsRatioEntity>()
        val contributionYears = ArrayList<ContributionsYearEntity>()

        // Date range more than a year is not allowed in this api
        // So we have to deal with multiple requests
        for (year in creationYear..currentYear) {

            contributionYears.add(ContributionsYearEntity(year))

            val contributionsData = getContributionsForDateRange(year)
            // Transform apollo result into room cache
            val cachedContributionsData = contributionsMapper.transform(contributionsData)
            contributionDaysCached.addAll(cachedContributionsData)

            // Sync ratio of total contributions (commits, PRs, etc.)
            val ratioData = getContributionsRatioForDateRange(year)
            val totalContributions = cachedContributionsData.sumOf { it.count }
            contributionsRatioCached.add(ratioMapper.transform(ratioData, totalContributions))
        }

        contributionsDao.clearContributionDays()
        contributionsDao.insertContributionDays(contributionDaysCached)
        contributionsDao.clearContributionYears()
        contributionsDao.insertContributionYearsCache(contributionYears)
        contributionsDao.clearContributionsRatios()
        contributionsDao.insertContributionsRatios(contributionsRatioCached)
    }


    private suspend fun syncProfileData() {
        val data = performApolloRequest(ProfileQuery())
        val cachedProfile = profileMapper.transform(data)
        userDao.clearUser()
        userDao.insertUser(cachedProfile)
    }

    private suspend fun syncRepositories() {
        val data = performApolloRequest(RepositoriesQuery())
        val cachedRepositories = repositoriesMapper.transform(data)
        reposDao.insertRepositories(cachedRepositories)
    }


    private suspend fun syncContributionRateData() {

        val contributionDays = contributionsDao.getContributionDaysList()
        val rates = ArrayList<ContributionRateEntity>()

        contributionDays.forEachIndexed { position, day ->
            val daysSlice = contributionDays.slice(0..position)
            val daysSliceContributionsCount = daysSlice.sumOf { it.count }

            // Don't count rate for the future :)
            // Set 0 by default
            // TODO check with changing timezones
            var rate = 0F
            if (day.date <= timeHelper.getBeginningOfDayForUnixDate(System.currentTimeMillis())) {
                rate = round((daysSliceContributionsCount.toFloat() / daysSlice.size.toFloat() * 100)) / 100F
            }

            Timber.d("count " + daysSliceContributionsCount.toFloat() + " / " + daysSlice.size.toFloat() + " * 100 / 100f")

            rates.add(
                ContributionRateEntity(
                    date = day.date,
                    rate = rate,
                    yearId = day.yearId
                )
            )
        }

        contributionsDao.clearContributionsYearsWithRatesCache()
        contributionsDao.insertContributionRatesCache(rates)
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


    // TODO dry
    private suspend fun getContributionsRatioForDateRange(year: Int?): ContributionsRatioQuery.Data {

        return if (year == null) {
            performApolloRequest(ContributionsRatioQuery(date_from = null, date_to = null))
        }
        else {
            val iso8601Year = timeHelper.getDatesRangeForYear_iso8601(year)
            performApolloRequest(
                ContributionsRatioQuery(
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

    fun getLastCacheSyncDateText(): String {
        return timeHelper.getFullFromUnixDate(appPreferences.lastSuccessCacheSyncDate)
    }

    fun checkForCache(): Boolean {
        return appPreferences.lastSuccessCacheSyncDate != 0L
    }

    private fun saveSyncTime() {
        // TODO check for timezone
        appPreferences.lastSuccessCacheSyncDate = System.currentTimeMillis()
    }

    private fun clearSyncTime() {
        appPreferences.lastSuccessCacheSyncDate = 0
    }

}