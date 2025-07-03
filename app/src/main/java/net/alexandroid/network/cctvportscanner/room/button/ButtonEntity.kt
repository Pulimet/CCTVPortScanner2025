package net.alexandroid.network.cctvportscanner.room.button

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "buttons",
    indices = [Index(value = ["title"], unique = true)]
)
data class ButtonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    val ports: String,
)
