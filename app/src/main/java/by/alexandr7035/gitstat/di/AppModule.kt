package by.alexandr7035.gitstat.di

import android.app.Application
import androidx.room.Room
import by.alexandr7035.gitstat.core.KeyValueStorage
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.*
import by.alexandr7035.gitstat.data.helpers.YearlyMetricsHelper
import by.alexandr7035.gitstat.data.local.CacheDB
import by.alexandr7035.gitstat.data.local.RoomTypeConverters
import by.alexandr7035.gitstat.data.local.dao.ContributionsDao
import by.alexandr7035.gitstat.data.local.dao.RepositoriesDao
import by.alexandr7035.gitstat.data.local.dao.UserDao
import by.alexandr7035.gitstat.data.remote.AuthInterceptor
import by.alexandr7035.gitstat.data.remote.ErrorInterceptor
import by.alexandr7035.gitstat.data.remote.mappers.*
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.HttpNetworkTransport
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /////////////////////////////////////
    // Network
    /////////////////////////////////////

    @Provides
    @Singleton
    fun provideApollo(keyValueStorage: KeyValueStorage): ApolloClient {
        return ApolloClient(networkTransport = HttpNetworkTransport("https://api.github.com/graphql", interceptors = listOf(AuthInterceptor(keyValueStorage), ErrorInterceptor())))
    }

    /////////////////////////////////////
    // Repositories
    /////////////////////////////////////

    @Provides
    @Singleton
    fun provideReposRepository(
        dao: RepositoriesDao,
        keyValueStorage: KeyValueStorage,
        gson: Gson): ReposRepository {
        return ReposRepository(dao, keyValueStorage, gson)
    }

    @Provides
    @Singleton
    fun provideUserRepository(dao: UserDao): UserRepository {
        return UserRepository(dao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(keyValueStorage: KeyValueStorage): AuthRepository {
        return AuthRepository(keyValueStorage)
    }

    @Provides
    @Singleton
    fun provideContributionsRepository(
        dao: ContributionsDao,
        yearlyMetricsHelper: YearlyMetricsHelper
    ): ContributionsRepository{
        return ContributionsRepository(dao, yearlyMetricsHelper)
    }


    @Provides
    @Singleton
    fun provideDataSyncRepository(
        apolloClient: ApolloClient,
        db: CacheDB,
        timeHelper: TimeHelper,
        keyValueStorage: KeyValueStorage,

        profileMapper: UserRemoteToCacheMapper,
        repositoriesMapper: RepositoriesRemoteToCacheMapper,
        contributionsMapper: ContributionsDaysListRemoteToCacheMapper,
        contributionTypesMapper: ContributionTypesRemoteToCacheMapper,
        daysToRatesMapper: ContributionDaysToRatesMapper
    ): DataSyncRepository {
        return DataSyncRepository(apolloClient, db, timeHelper, keyValueStorage, profileMapper, repositoriesMapper, contributionsMapper, contributionTypesMapper, daysToRatesMapper)
    }

    /////////////////////////////////////
    // Mappers
    /////////////////////////////////////

    @Provides
    fun provideUserMapper(timeHelper: TimeHelper): UserRemoteToCacheMapper {
        return UserRemoteToCacheMapper(timeHelper)
    }

    @Provides
    fun provideReposMapper(timeHelper: TimeHelper): RepositoriesRemoteToCacheMapper {
        return RepositoriesRemoteToCacheMapper(timeHelper)
    }

    @Provides
    fun provideContributionDayMapper(timeHelper: TimeHelper): ContributionDayRemoteToCacheMapper {
        return ContributionDayRemoteToCacheMapper(timeHelper)
    }

    @Provides
    fun provideContributionTypesMapper(timeHelper: TimeHelper): ContributionTypesRemoteToCacheMapper {
        return ContributionTypesRemoteToCacheMapper(timeHelper)
    }

    @Provides
    fun provideContributionDaysToRatesMapper(timeHelper: TimeHelper): ContributionDaysToRatesMapper {
        return ContributionDaysToRatesMapper(timeHelper)
    }

    /////////////////////////////////////
    // Preferences
    /////////////////////////////////////

    @Provides
    @Singleton
    fun provideAppPrefs(application: Application): KeyValueStorage {
        return AppPreferences(application)
    }

    /////////////////////////////////////
    // Database
    /////////////////////////////////////

    @Provides
    @Singleton
    fun provideRoomDb(application: Application, gson: Gson): CacheDB {
        return Room
            .databaseBuilder(application.applicationContext, CacheDB::class.java, "cache.db")
            .addTypeConverter(RoomTypeConverters(gson))
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: CacheDB): UserDao {
        return db.getUserDao()
    }

    @Provides
    @Singleton
    fun provideRepositoriesDao(db: CacheDB): RepositoriesDao {
        return db.getRepositoriesDao()
    }

    @Provides
    @Singleton
    fun provideContributionsDao(db: CacheDB): ContributionsDao {
        return db.getContributionsDao()
    }

    /////////////////////////////////////
    // Helpers
    /////////////////////////////////////

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideTimeHelper(): TimeHelper {
        return TimeHelper()
    }

    @Provides
    @Singleton
    fun provideYearlyMetricsHelper(keyValueStorage: KeyValueStorage, timeHelper: TimeHelper): YearlyMetricsHelper {
        return YearlyMetricsHelper.Impl(timeHelper, keyValueStorage)
    }
}