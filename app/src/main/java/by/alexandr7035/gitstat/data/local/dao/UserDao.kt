package by.alexandr7035.gitstat.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.alexandr7035.gitstat.data.local.model.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("select * from user")
    fun getUser(): UserEntity

    @Query("select * from user")
    fun getUserLiveData(): LiveData<UserEntity>

    @Query("DELETE FROM user")
    suspend fun clearUser()

}
