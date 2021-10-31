package by.alexandr7035.gitstat.di

import android.app.Application
import androidx.room.Room
import by.alexandr7035.gitstat.core.AppPreferences
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.*
import by.alexandr7035.gitstat.data.local.CacheDB
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
    fun provideApollo(appPreferences: AppPreferences): ApolloClient {
        return ApolloClient(networkTransport = HttpNetworkTransport("https://api.github.com/graphql", interceptors = listOf(AuthInterceptor(appPreferences), ErrorInterceptor())))
    }

    /////////////////////////////////////
    // Repositories
    /////////////////////////////////////

    @Provides
    @Singleton
    fun provideReposRepository(
        dao: RepositoriesDao,
        appPreferences: AppPreferences,
        gson: Gson): ReposRepository {
        return ReposRepository(dao, appPreferences, gson)
    }

    @Provides
    @Singleton
    fun provideUserRepository(dao: UserDao): UserRepository {
        return UserRepository(dao)
    }

    @Provides
    @Singleton
    fun provideSyncRepository(
        apolloClient: ApolloClient,
        userDao: UserDao,
        contributionsDao: ContributionsDao,
        repositoriesDao: RepositoriesDao,
        profileMapper: UserRemoteToCacheMapper,
        repositoriesMapper: RepositoriesRemoteToCacheMapper,
        contributionsMapper: ContributionsDaysListRemoteToCacheMapper,
        ratioMapper: ContributionsRatioRemoteToCacheMapper,
        timeHelper: TimeHelper,
        appPreferences: AppPreferences): SyncRepository {
        return SyncRepository(apolloClient, userDao, repositoriesDao, contributionsDao, profileMapper, repositoriesMapper, contributionsMapper, ratioMapper, timeHelper, appPreferences)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(apolloClient: ApolloClient, appPreferences: AppPreferences): AuthRepository {
        return AuthRepository(appPreferences)
    }

    @Provides
    @Singleton
    fun provideContributionsRepository(
        dao: ContributionsDao,
        timeHelper: TimeHelper,
        appPreferences: AppPreferences
    ): ContributionsRepository{
        return ContributionsRepository(dao, timeHelper, appPreferences)
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
    fun provideContributionsRatioMapper(timeHelper: TimeHelper): ContributionsRatioRemoteToCacheMapper {
        return ContributionsRatioRemoteToCacheMapper(timeHelper)
    }

    /////////////////////////////////////
    // Preferences
    /////////////////////////////////////

    @Provides
    @Singleton
    fun provideAppPrefs(application: Application): AppPreferences {
        return AppPreferences(application)
    }

    /////////////////////////////////////
    // Database
    /////////////////////////////////////

    @Provides
    @Singleton
    fun provideRoomDb(application: Application): CacheDB {
        return Room
            .databaseBuilder(application.applicationContext, CacheDB::class.java, "cache.db")
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
}