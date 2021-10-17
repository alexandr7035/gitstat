package com.alexandr7035.gitstat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexandr7035.gitstat.data.local.dao.ContributionsDao
import com.alexandr7035.gitstat.data.local.dao.RepositoriesDao
import com.alexandr7035.gitstat.data.local.dao.UserDao
import com.alexandr7035.gitstat.data.local.model.*

@Database(entities = [UserEntity::class,
    RepositoryEntity::class,
    ContributionDayEntity::class,
    ContributionRateEntity::class,
    ContributionsYearEntity::class,
    ContributionsRatioEntity::class], version = 15)

abstract class CacheDB : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getRepositoriesDao(): RepositoriesDao

    abstract fun getContributionsDao(): ContributionsDao
}