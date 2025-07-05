package net.alexandroid.network.cctvportscanner.room.button

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ButtonDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(host: ButtonEntity)

    @Delete
    suspend fun delete(item: ButtonEntity)

    @Update
    suspend fun update(item: ButtonEntity)

    @Query("SELECT * from buttons ORDER BY title ASC")
    fun getAllItemsFlow(): Flow<List<ButtonEntity>>

    suspend fun insertAll(listOf: List<ButtonEntity>) {
        listOf.forEach { insert(it) }
    }
}