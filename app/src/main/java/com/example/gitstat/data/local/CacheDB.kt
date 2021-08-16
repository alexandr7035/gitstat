package com.example.gitstat.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import okhttp3.Cache

@Database(entities = [UserEntity::class], version = 1)
abstract class CacheDB : RoomDatabase() {

    abstract fun getDao(): CacheDao

    companion object {

        private var sInstance: CacheDB? = null
        private const val dbName: String = "cache.db"

        // Ge the singleton instance of SampleDatabase
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