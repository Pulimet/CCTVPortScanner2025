package net.alexandroid.network.cctvportscanner.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hosts")
data class HostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val hostName: String,
)
