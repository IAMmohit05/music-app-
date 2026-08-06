package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSeconds: Int,
    val albumArtUrl: String,
    val audioUrl: String = "",
    val genre: String,
    val isTrending: Boolean = false,
    val isTopChart: Boolean = false,
    val lyrics: String = "Instrumental or lyrics unavailable for this track.",
    val isLiked: Boolean = false
) {
    val durationFormatted: String
        get() {
            val minutes = durationSeconds / 60
            val seconds = durationSeconds % 60
            return String.format("%d:%02d", minutes, seconds)
        }
}

enum class AudioQuality(val label: String, val bitrate: String) {
    STANDARD("Standard", "128 kbps"),
    HIGH("High Quality", "320 kbps"),
    ULTRA_HD("Ultra HD", "24-bit / 192kHz")
}

enum class RepeatMode {
    NONE,
    REPEAT_ALL,
    REPEAT_ONE
}
