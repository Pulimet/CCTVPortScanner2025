package net.alexandroid.network.cctvportscanner.room

import androidx.room.Database
import androidx.room.RoomDatabase
import net.alexandroid.network.cctvportscanner.room.button.ButtonDao
import net.alexandroid.network.cctvportscanner.room.host.HostDao
import net.alexandroid.network.cctvportscanner.room.host.HostEntity

@Database(entities = [HostEntity::class], version = 1, exportSchema = false)
abstract class ScannerDatabase : RoomDatabase() {
    abstract fun hostDao(): HostDao
    abstract fun buttonDao(): ButtonDao
}