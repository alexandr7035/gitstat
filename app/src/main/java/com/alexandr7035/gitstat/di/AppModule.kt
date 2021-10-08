package com.alexandr7035.gitstat.di

import android.app.Application
import androidx.room.Room
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.core.TimeHelper
import com.alexandr7035.gitstat.data.*
import com.alexandr7035.gitstat.data.local.CacheDB
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.mappers.ContributionsDaysToYearsMapper
import com.alexandr7035.gitstat.data.remote.ApolloInterceptor
import com.alexandr7035.gitstat.data.remote.ErrorInterceptor
import com.alexandr7035.gitstat.data.remote.mappers.ContributionDayRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.ContributionsDaysListRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.RepositoriesRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.UserRemoteToCacheMapper
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

    @Provides
    @Singleton
    fun provideApollo(appPreferences: AppPreferences): ApolloClient {
        return ApolloClient(networkTransport = HttpNetworkTransport("https://api.github.com/graphql", interceptors = listOf(ApolloInterceptor(appPreferences), ErrorInterceptor())))
    }


    @Provides
    @Singleton
    fun provideReposRepository(
        dao: CacheDao,
        appPreferences: AppPreferences,
        gson: Gson): ReposRepository {
        return ReposRepository(dao, appPreferences, gson)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(appPreferences: AppPreferences, dao: CacheDao): LoginRepository {
        return LoginRepository(appPreferences, dao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(dao: CacheDao): UserRepository {
        return UserRepository(dao)
    }

    @Provides
    fun provideUserMapper(timeHelper: TimeHelper): UserRemoteToCacheMapper {
        return UserRemoteToCacheMapper(timeHelper)
    }

    @Provides
    @Singleton
    fun provideSyncRepository(
        apolloClient: ApolloClient,
        dao: CacheDao,
        profileMapper: UserRemoteToCacheMapper,
        repositoriesMapper: RepositoriesRemoteToCacheMapper,
        contributionsMapper: ContributionsDaysListRemoteToCacheMapper,
        timeHelper: TimeHelper,
        appPreferences: AppPreferences): SyncRepository {
        return SyncRepository(apolloClient, dao, profileMapper, repositoriesMapper, contributionsMapper, timeHelper, appPreferences)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(apolloClient: ApolloClient, appPreferences: AppPreferences): AuthRepository {
        return AuthRepository(apolloClient, appPreferences)
    }

    @Provides
    @Singleton
    fun provideContributionsRepository(dao: CacheDao, toYearsMapper: ContributionsDaysToYearsMapper): ContributionsRepository{
        return ContributionsRepository(dao, toYearsMapper)
    }

    @Provides
    fun provideReposMapper(timeHelper: TimeHelper): RepositoriesRemoteToCacheMapper {
        return RepositoriesRemoteToCacheMapper(timeHelper)
    }

    @Provides
    fun provideContributionDayMapper(): ContributionDayRemoteToCacheMapper {
        return ContributionDayRemoteToCacheMapper()
    }

    @Provides
    @Singleton
    fun provideAppPrefs(application: Application): AppPreferences {
        return AppPreferences(application)
    }

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
    fun provideRoomDao(db: CacheDB): CacheDao {
        return db.getDao()
    }

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