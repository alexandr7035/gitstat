package com.alexandr7035.gitstat.di

import android.app.Application
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.core.ProgLangManager
import com.alexandr7035.gitstat.data.LoginRepository
import com.alexandr7035.gitstat.data.ReposRepository
import com.alexandr7035.gitstat.data.UserRepository
import com.alexandr7035.gitstat.data.local.CacheDB
import com.alexandr7035.gitstat.data.local.CacheDao
import com.alexandr7035.gitstat.data.remote.GitHubApi
import com.alexandr7035.gitstat.data.remote.NetworkModule
import com.alexandr7035.gitstat.data.remote.mappers.RepositoryRemoteToCacheMapper
import com.alexandr7035.gitstat.data.remote.mappers.UserRemoteToCacheMapper
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
    fun provideApi(retrofit: Retrofit): GitHubApi {
        return retrofit.create(GitHubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkModule(appPreferences: AppPreferences, gitHubApi: GitHubApi): NetworkModule {
        return NetworkModule(appPreferences, gitHubApi)
    }

    @Provides
    @Singleton
    fun provideReposRepository(
        networkModule: NetworkModule,
        dao: CacheDao,
        repositoryMapper: RepositoryRemoteToCacheMapper,
        appPreferences: AppPreferences,
        progLangManager: ProgLangManager,
        gson: Gson): ReposRepository {
        return ReposRepository(networkModule, dao, repositoryMapper, appPreferences, gson, progLangManager)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(appPreferences: AppPreferences, networkModule: NetworkModule): LoginRepository {
        return LoginRepository(appPreferences, networkModule)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: NetworkModule, dao: CacheDao, userMapper: UserRemoteToCacheMapper): UserRepository {
        return UserRepository(api, dao, userMapper)
    }

    @Provides
    fun provideUserMapper(): UserRemoteToCacheMapper {
        return UserRemoteToCacheMapper()
    }

    @Provides
    fun provideReposMapper(): RepositoryRemoteToCacheMapper {
        return RepositoryRemoteToCacheMapper()
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
}