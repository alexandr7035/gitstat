package by.alexandr7035.gitstat.data

import androidx.lifecycle.MutableLiveData
import by.alexandr7035.gitstat.apollo.ContributionTypesQuery
import by.alexandr7035.gitstat.apollo.ContributionsQuery
import by.alexandr7035.gitstat.apollo.PinnedRepositoriesQuery
import by.alexandr7035.gitstat.apollo.ProfileCreationDateQuery
import by.alexandr7035.gitstat.apollo.ProfileQuery
import by.alexandr7035.gitstat.apollo.RepositoriesQuery
import by.alexandr7035.gitstat.core.AppError
import by.alexandr7035.gitstat.core.DataSyncStatus
import by.alexandr7035.gitstat.core.extensions.performRequestWithDataResult
import by.alexandr7035.gitstat.core.helpers.TimeHelper
import by.alexandr7035.gitstat.data.local.CacheDB
import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import by.alexandr7035.gitstat.data.local.model.ContributionRateEntity
import by.alexandr7035.gitstat.data.local.model.ContributionTypesEntity
import by.alexandr7035.gitstat.data.local.model.ContributionsMonthEntity
import by.alexandr7035.gitstat.data.local.model.ContributionsYearEntity
import by.alexandr7035.gitstat.data.local.model.RepositoryEntity
import by.alexandr7035.gitstat.data.local.model.UserEntity
import by.alexandr7035.gitstat.data.local.preferences.AppPreferences
import by.alexandr7035.gitstat.data.remote.mappers.ContributionDaysToRatesMapper
import by.alexandr7035.gitstat.data.remote.mappers.ContributionTypesRemoteToCacheMapper
import by.alexandr7035.gitstat.data.remote.mappers.ContributionsDaysListRemoteToCacheMapper
import by.alexandr7035.gitstat.data.remote.mappers.RepositoriesRemoteToCacheMapper
import by.alexandr7035.gitstat.data.remote.mappers.UserRemoteToCacheMapper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import timber.log.Timber
import java.util.TreeMap
import javax.inject.Inject

class DataSyncRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val db: CacheDB,
    private val timeHelper: TimeHelper,
    private val appPreferences: AppPreferences,

    private val profileMapper: UserRemoteToCacheMapper,
    private val repositoriesMapper: RepositoriesRemoteToCacheMapper,
    private val contributionsMapper: ContributionsDaysListRemoteToCacheMapper,
    private val contributionTypesMapper: ContributionTypesRemoteToCacheMapper,
    private val daysToRatesMapper: ContributionDaysToRatesMapper,
) {

    // Set livedata to non-null when need to observe sync status on UI
    // Null by default
    suspend fun syncData(syncStatusLiveData: MutableLiveData<DataSyncStatus>? = null) {

        try {
            syncStatusLiveData?.postValue(DataSyncStatus.PendingProfile)

            // Get creation and current years
            val profileCreationDate = apolloClient.performRequestWithDataResult(ProfileCreationDateQuery()).viewer.createdAt as String
            val unixCreationDate = timeHelper.getUnixDateFromISO8601(profileCreationDate)
            val accountCreationYear = timeHelper.getYearFromUnixDate(unixCreationDate)
            val currentYear = timeHelper.getYearFromUnixDate(System.currentTimeMillis())

            val profile = fetchProfile()

            syncStatusLiveData?.postValue(DataSyncStatus.PendingRepos)
            val repositories = fetchRepositories()

            syncStatusLiveData?.postValue(DataSyncStatus.PendingContributions)
            val contributionDays = fetchContributionDays(accountCreationYear, currentYear)
            val contributionTypes = fetchContributionTypes(accountCreationYear, currentYear, contributionDays)
            val contributionRates = fetchContributionRates(contributionDays)
            val contributionYears = fetchContributionYears(accountCreationYear, currentYear)
            val contributionMonths = fetchContributionMonths(contributionDays)

            // Write cache
            clearCache()

            // Do not put it after cache saving in db
            // As it may cause bugs with calculations
            // (the milliseconds when livedata is written and triggered but the date is not updated)
            appPreferences.saveLastCacheSyncDate(System.currentTimeMillis())

            Timber.tag("DEBUG_TAG").d("profile $profile")
            db.getUserDao().insertUser(profile)
            db.getRepositoriesDao().insertRepositories(repositories)

            // NOTE! DO NOT CHANGE ORDER
            // LIVEDATA TRIGGERED IMMEDIATELY AFTER SAVING CACHE
            // AS ONE DATA MAY DEPEND ON THE OTHER, WRONG ORDER MAY CAUSE CRASH
            // Order must be: days -> months -> years
            db.getContributionsDao().apply {
                insertContributionRatesCache(contributionRates)
                insertContributionDays(contributionDays)
                insertContributionTypes(contributionTypes)
                insertContributionMonthsCache(contributionMonths)
                insertContributionYearsCache(contributionYears)
            }

            syncStatusLiveData?.postValue(DataSyncStatus.Success)
        }

        catch (e: AppError) {
            Timber.tag("DEBUG_SYNC").e("Catch exception during data sync ${e.type.name}")
            syncStatusLiveData?.postValue(DataSyncStatus.Failure(e.type))
        }
    }

    private suspend fun fetchProfile(): UserEntity {
        val data = apolloClient.performRequestWithDataResult(ProfileQuery())
        return profileMapper.map(data)
    }

    private suspend fun fetchRepositories(): List<RepositoryEntity> {
        val repositories = ArrayList<RepositoryEntity>()
        var nextPagesExist = true
        var endCursor: String? = null

        val pinnedRepositoriesData = (apolloClient
            .performRequestWithDataResult(PinnedRepositoriesQuery())
            .viewer.pinnedItems.nodes)?.filterNotNull() ?: emptyList()

        val pinnedIds = (pinnedRepositoriesData.filter {
            it.onRepository != null
        }).mapNotNull {
            it.onRepository?.databaseId
        }

        while (nextPagesExist) {
            val data = apolloClient.performRequestWithDataResult(RepositoriesQuery(Optional.present(endCursor)))
            repositories.addAll(repositoriesMapper.map(repositories = data, pinnedItems = pinnedIds))

            nextPagesExist = data.viewer.repositories.pageInfo.hasNextPage
            endCursor = data.viewer.repositories.pageInfo.endCursor
        }

        return repositories
    }

    private fun fetchContributionYears(accountCreationYear: Int, currentYear: Int): List<ContributionsYearEntity> {
        val years = ArrayList<ContributionsYearEntity>()

        for (year in accountCreationYear..currentYear) {
            years.add(ContributionsYearEntity(year))
        }

        return years
    }

    private fun fetchContributionMonths(contributionDays: List<ContributionDayEntity>): ArrayList<ContributionsMonthEntity> {

        // Use TreeMap as it is always sorted by key
        val monthsMap: TreeMap<Int, Int> = TreeMap()
        val months = ArrayList<ContributionsMonthEntity>()

        for (day in contributionDays) {
            monthsMap[day.monthId] = day.yearId
        }

        for ((id, yearId) in monthsMap) {
            months.add(ContributionsMonthEntity(id, yearId))
        }

        return months
    }

    private suspend fun fetchContributionDays(profileCreationYear: Int, currentYear: Int): List<ContributionDayEntity> {

        val contributionDaysCached = ArrayList<ContributionDayEntity>()

        // Date range more than a year is not allowed in this api
        // So we have to deal with multiple requests
        for (year in profileCreationYear..currentYear) {

            val contributionsData = getContributionsForDateRange(year)
            // Map apollo result into room cache
            val cachedContributionsData = contributionsMapper.map(contributionsData)
            contributionDaysCached.addAll(cachedContributionsData)
        }

        return contributionDaysCached
    }


    // TODO try to find better solution later
    private suspend fun fetchContributionTypes(
        profileCreationYear: Int, currentYear: Int, cachedContributions: List<ContributionDayEntity>
    ): ArrayList<ContributionTypesEntity> {

        val contributionTypesCached = ArrayList<ContributionTypesEntity>()

        // Date range more than a year is not allowed in this api
        // So we have to deal with multiple requests
        for (year in profileCreationYear..currentYear) {

            // We need to get totalContributions here and pass them to mapper
            // Some of contributions do not belong to 5 groups (commits, issues, PRs, code reviews, repositories)
            // that Github API provides. We mark them as "Unknown" in mapper
            val totalContributionsCount = cachedContributions.filter { it.yearId == year }.sumOf { it.count }

            val typesData = getContributionTypesForDateRange(year)
            contributionTypesCached.add(contributionTypesMapper.transform(typesData, totalContributionsCount))
        }

        return contributionTypesCached
    }


    private fun fetchContributionRates(contributionDays: List<ContributionDayEntity>): List<ContributionRateEntity> {
        return daysToRatesMapper.map(contributionDays)
    }


    // Pass null to get contributions for the last year
    // FIXME DRY
    private suspend fun getContributionsForDateRange(year: Int?): ContributionsQuery.Data {

        return if (year == null) {
            apolloClient.performRequestWithDataResult(
                ContributionsQuery(
                    date_from = Optional.Present(null),
                    date_to = Optional.Present(null)
                )
            )
        } else {
            val iso8601Year = timeHelper.getDatesRangeForYear_iso8601(year)
            apolloClient.performRequestWithDataResult(
                ContributionsQuery(
                    date_from = Optional.Present(iso8601Year.startDate),
                    date_to = Optional.present(iso8601Year.endDate)
                )
            )
        }
    }


    private suspend fun getContributionTypesForDateRange(year: Int?): ContributionTypesQuery.Data {

        return if (year == null) {
            apolloClient.performRequestWithDataResult(
                ContributionTypesQuery(
                    date_from = Optional.Present(null), date_to = Optional.Present(null)
                )
            )
        } else {
            val iso8601Year = timeHelper.getDatesRangeForYear_iso8601(year)
            apolloClient.performRequestWithDataResult(
                ContributionTypesQuery(
                    date_from = Optional.Present(iso8601Year.startDate), date_to = Optional.Present(iso8601Year.endDate)
                )
            )
        }
    }

    fun checkIfCacheExists(): Boolean {
        return appPreferences.getLastCacheSyncDate() != 0L
    }

    fun checkIfTokenSaved(): Boolean {
        return appPreferences.getToken() != null
    }

    fun clearCache() {
        // Be patient with livedata. This call causes updates with null
        // So wrap code in observers with null check
        db.clearAllTables()
        appPreferences.saveLastCacheSyncDate(0)
    }

    fun clearToken() {
        appPreferences.saveToken(null)
    }

    fun getLastCacheSyncDateText(): String {
        return timeHelper.getFullFromUnixDate(appPreferences.getLastCacheSyncDate())
    }

}