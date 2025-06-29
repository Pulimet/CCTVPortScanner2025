package net.alexandroid.network.cctvportscanner.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HostEntity::class], version = 1, exportSchema = false)
abstract class ScannerDatabase : RoomDatabase() {
    abstract fun hostDao(): HostDao
}