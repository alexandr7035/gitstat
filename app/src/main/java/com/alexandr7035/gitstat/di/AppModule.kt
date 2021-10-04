package com.alexandr7035.gitstat.di

import android.app.Application
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.core.DaggerApp_HiltComponents_SingletonC.builder
import com.alexandr7035.gitstat.core.ProgLangManager
import com.alexandr7035.gitstat.core.TimeHelper
import com.alexandr7035.gitstat.data.*
import com.alexandr7035.gitstat.data.local.CacheDB
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.local.mappers.ContributionsDaysToYearsMapper
import com.alexandr7035.gitstat.data.remote.ApolloInterceptor
import com.alexandr7035.gitstat.data.remote.RestApi
import com.alexandr7035.gitstat.data.remote.RestApiHelper
import com.alexandr7035.gitstat.data.remote.mappers.ContributionDayRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.ContributionsDaysListRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.RepositoryRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.UserRemoteToCacheMapper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.HttpNetworkTransport
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return  OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
            .retryOnConnectionFailure(false)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApollo(appPreferences: AppPreferences): ApolloClient {
        return ApolloClient(networkTransport = HttpNetworkTransport("https://api.github.com/graphql", interceptors = listOf(ApolloInterceptor(appPreferences))))
    }

    @Provides
    @Singleton
    fun provideRestApi(retrofit: Retrofit): RestApi {
        return retrofit.create(RestApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRestApiHelper(appPreferences: AppPreferences, restApi: RestApi): RestApiHelper {
        return RestApiHelper(appPreferences, restApi)
    }

    @Provides
    @Singleton
    fun provideReposRepository(
        restApiHelper: RestApiHelper,
        dao: CacheDao,
        repositoryMapper: RepositoryRemoteToCacheMapper,
        appPreferences: AppPreferences,
        progLangManager: ProgLangManager,
        gson: Gson): ReposRepository {
        return ReposRepository(restApiHelper, dao, repositoryMapper, appPreferences, gson, progLangManager)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(appPreferences: AppPreferences, restApiHelper: RestApiHelper, dao: CacheDao): LoginRepository {
        return LoginRepository(appPreferences, restApiHelper, dao)
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
        contributionsMapper: ContributionsDaysListRemoteToCacheMapper,
        timeHelper: TimeHelper): SyncRepository {
        return SyncRepository(apolloClient, dao, profileMapper, contributionsMapper, timeHelper)
    }

    @Provides
    @Singleton
    fun provideContributionsRepository(dao: CacheDao, toYearsMapper: ContributionsDaysToYearsMapper): ContributionsRepository{
        return ContributionsRepository(dao, toYearsMapper)
    }

    @Provides
    fun provideReposMapper(): RepositoryRemoteToCacheMapper {
        return RepositoryRemoteToCacheMapper()
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
        return CacheDB.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideRoomDao(db: CacheDB): CacheDao {
        return db.getDao()
    }

    @Provides
    @Singleton
    fun provideLanguagesManager(application: Application): ProgLangManager{
        return ProgLangManager(application)
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