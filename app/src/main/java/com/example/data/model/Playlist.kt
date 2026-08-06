package com.example.data.model

data class Playlist(
    val id: String,
    val name: String,
    val description: String,
    val coverUrl: String,
    val songs: List<Song> = emptyList(),
    val isCustom: Boolean = false
)

data class GenreCategory(
    val id: String,
    val name: String,
    val hexColor: String,
    val iconName: String
)
