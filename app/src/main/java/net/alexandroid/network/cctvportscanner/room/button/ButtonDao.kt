package net.alexandroid.network.cctvportscanner.room.button

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ButtonDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(host: ButtonEntity)

    @Delete
    suspend fun delete(item: ButtonEntity)

    @Query("SELECT * from hosts ORDER BY host_name ASC")
    suspend fun getAllItems(): List<ButtonEntity>

    @Query("SELECT * from hosts ORDER BY host_name ASC")
    fun getAllItemsFlow(): Flow<List<ButtonEntity>>
}