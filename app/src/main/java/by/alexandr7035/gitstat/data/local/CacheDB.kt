package by.alexandr7035.gitstat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import by.alexandr7035.gitstat.data.local.dao.ContributionsDao
import by.alexandr7035.gitstat.data.local.dao.RepositoriesDao
import by.alexandr7035.gitstat.data.local.dao.UserDao
import by.alexandr7035.gitstat.data.local.model.*

@Database(entities = [UserEntity::class,
    RepositoryEntity::class,
    ContributionDayEntity::class,
    ContributionRateEntity::class,
    ContributionsYearEntity::class,
    ContributionTypesEntity::class,
    ContributionsMonthEntity::class], version = 20)

abstract class CacheDB : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getRepositoriesDao(): RepositoriesDao

    abstract fun getContributionsDao(): ContributionsDao
}