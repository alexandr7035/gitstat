package com.alexandr7035.gitstat.di

import android.app.Application
import androidx.room.Room
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.core.TimeHelper
import com.alexandr7035.gitstat.data.*
import com.alexandr7035.gitstat.data.local.CacheDB
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.remote.AuthInterceptor
import com.alexandr7035.gitstat.data.remote.ErrorInterceptor
import com.alexandr7035.gitstat.data.remote.mappers.*
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
        return ApolloClient(networkTransport = HttpNetworkTransport("https://api.github.com/graphql", interceptors = listOf(AuthInterceptor(appPreferences), ErrorInterceptor())))
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
        ratioMapper: ContributionsRatioRemoteToCacheMapper,
        timeHelper: TimeHelper,
        appPreferences: AppPreferences): SyncRepository {
        return SyncRepository(apolloClient, dao, profileMapper, repositoriesMapper, contributionsMapper, ratioMapper, timeHelper, appPreferences)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(apolloClient: ApolloClient, appPreferences: AppPreferences): AuthRepository {
        return AuthRepository(apolloClient, appPreferences)
    }

    @Provides
    @Singleton
    fun provideContributionsRepository(dao: CacheDao): ContributionsRepository{
        return ContributionsRepository(dao)
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