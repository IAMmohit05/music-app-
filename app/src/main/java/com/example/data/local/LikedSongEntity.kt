package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_songs")
data class LikedSongEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSeconds: Int,
    val albumArtUrl: String,
    val genre: String,
    val lyrics: String,
    val addedTimestamp: Long = System.currentTimeMillis()
)
