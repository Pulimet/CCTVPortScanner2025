package net.alexandroid.network.cctvportscanner.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(host: HostEntity)

    @Delete
    suspend fun delete(item: HostEntity)

    @Query("SELECT * from hosts ORDER BY hostName ASC")
    suspend fun getAllItems(): List<HostEntity>

    @Query("SELECT * from hosts ORDER BY hostName ASC")
    fun getAllItemsFlow(): Flow<List<HostEntity>>
}