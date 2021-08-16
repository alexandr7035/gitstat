package com.example.gitstat.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCache(user: UserEntity)

    @Update
    suspend fun updateUserCache(user: UserEntity)

    @Delete
    suspend fun deleteUserCache(user: UserEntity)

    @Query("select * from user where id = (:id)")
    fun getUserCache(id: Long): LiveData<UserEntity>

}