package net.alexandroid.network.cctvportscanner.room.host

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hosts",
    indices = [Index(value = ["host_name"], unique = true)]
)
data class HostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "host_name")
    val hostName: String,
)