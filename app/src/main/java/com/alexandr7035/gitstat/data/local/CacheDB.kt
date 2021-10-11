package com.alexandr7035.gitstat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexandr7035.gitstat.data.local.model.*

@Database(entities = [UserEntity::class, RepositoryEntity::class, ContributionDayEntity::class, ContributionRateEntity::class, ContributionsYearEntity::class], version = 10)
abstract class CacheDB : RoomDatabase() {
    abstract fun getDao(): CacheDao
}