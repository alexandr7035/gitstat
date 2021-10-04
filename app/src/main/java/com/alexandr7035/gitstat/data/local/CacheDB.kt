package com.alexandr7035.gitstat.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.data.local.model.UserEntity

@Database(entities = [UserEntity::class, RepositoryEntity::class, ContributionDayEntity::class], version = 6)
abstract class CacheDB : RoomDatabase() {

    abstract fun getDao(): CacheDao

    companion object {

        @Volatile
        private var sInstance: CacheDB? = null
        private const val dbName: String = "cache.db"

        @Synchronized
        fun getInstance(context: Context): CacheDB {
            if (sInstance == null) {
                sInstance = Room
                    .databaseBuilder(context.applicationContext, CacheDB::class.java, dbName)
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return sInstance!!
        }
    }

}