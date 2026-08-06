package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val coverUrl: String,
    val songIdsJson: String, // Comma separated IDs or JSON string
    val isCustom: Boolean = true,
    val createdTimestamp: Long = System.currentTimeMillis()
)
