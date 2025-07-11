package net.alexandroid.network.cctvportscanner.room.host

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HostDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(host: HostEntity)

    @Delete
    suspend fun delete(item: HostEntity)

    @Query("SELECT * from hosts ORDER BY host_name ASC")
    fun getAllItemsFlow(): Flow<List<HostEntity>>
}