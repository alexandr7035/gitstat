package com.alexandr7035.gitstat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.data.local.model.UserEntity

@Database(entities = [UserEntity::class, RepositoryEntity::class, ContributionDayEntity::class], version = 7)
abstract class CacheDB : RoomDatabase() {
    abstract fun getDao(): CacheDao
}