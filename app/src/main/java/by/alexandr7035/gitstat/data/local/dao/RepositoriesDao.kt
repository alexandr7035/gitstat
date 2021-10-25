package by.alexandr7035.gitstat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.alexandr7035.gitstat.data.local.model.RepositoryEntity

@Dao
interface RepositoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repos: List<RepositoryEntity>)

    @Query("select * from repositories")
    suspend fun getRepositories(): List<RepositoryEntity>

    // TODO FIXME
    @Query("select * from repositories where not isArchived")
    suspend fun getActiveRepositories(): List<RepositoryEntity>

    @Query("select * from repositories where isArchived")
    suspend fun getArchivedRepositories(): List<RepositoryEntity>

    @Query("DELETE FROM repositories")
    suspend fun clearRepositories()
}