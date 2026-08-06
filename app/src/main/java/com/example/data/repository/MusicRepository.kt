package com.example.data.repository

import com.example.data.local.LikedSongEntity
import com.example.data.local.MusicDao
import com.example.data.local.PlaylistEntity
import com.example.data.model.AudioQuality
import com.example.data.model.MockData
import com.example.data.model.Playlist
import com.example.data.model.Song
import com.example.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class MusicRepository(private val musicDao: MusicDao) {

    // User session
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // App Preferences
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _audioQuality = MutableStateFlow(AudioQuality.HIGH)
    val audioQuality: StateFlow<AudioQuality> = _audioQuality.asStateFlow()

    private val _equalizerPreset = MutableStateFlow("Bass Boost")
    val equalizerPreset: StateFlow<String> = _equalizerPreset.asStateFlow()

    // Liked songs from Room DB
    val likedSongs: Flow<List<Song>> = musicDao.getAllLikedSongs().map { entities ->
        entities.map { entity ->
            Song(
                id = entity.id,
                title = entity.title,
                artist = entity.artist,
                album = entity.album,
                durationSeconds = entity.durationSeconds,
                albumArtUrl = entity.albumArtUrl,
                genre = entity.genre,
                lyrics = entity.lyrics,
                isLiked = true
            )
        }
    }

    // Playlists from Room DB
    val customPlaylists: Flow<List<Playlist>> = musicDao.getAllPlaylists().map { entities ->
        entities.map { entity ->
            val songIds = entity.songIdsJson.split(",").filter { it.isNotBlank() }
            val playlistSongs = MockData.sampleSongs.filter { songIds.contains(it.id) }
            Playlist(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                coverUrl = entity.coverUrl,
                songs = playlistSongs,
                isCustom = entity.isCustom
            )
        }
    }

    fun isSongLiked(songId: String): Flow<Boolean> = musicDao.isLiked(songId)

    suspend fun toggleLikeSong(song: Song) {
        val currentlyLiked = musicDao.isLikedSync(song.id)
        if (currentlyLiked) {
            musicDao.deleteLikedSong(song.id)
        } else {
            val entity = LikedSongEntity(
                id = song.id,
                title = song.title,
                artist = song.artist,
                album = song.album,
                durationSeconds = song.durationSeconds,
                albumArtUrl = song.albumArtUrl,
                genre = song.genre,
                lyrics = song.lyrics
            )
            musicDao.insertLikedSong(entity)
        }
    }

    suspend fun createCustomPlaylist(name: String, description: String, coverUrl: String, initialSongs: List<Song>) {
        val id = "custom_pl_${System.currentTimeMillis()}"
        val songIds = initialSongs.joinToString(",") { it.id }
        val entity = PlaylistEntity(
            id = id,
            name = name,
            description = description,
            coverUrl = if (coverUrl.isBlank()) "https://picsum.photos/id/1040/500/500" else coverUrl,
            songIdsJson = songIds,
            isCustom = true
        )
        musicDao.insertPlaylist(entity)
    }

    fun setUser(user: User?) {
        _currentUser.value = user
    }

    fun toggleDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }

    fun setAudioQuality(quality: AudioQuality) {
        _audioQuality.value = quality
    }

    fun setEqualizerPreset(preset: String) {
        _equalizerPreset.value = preset
    }
}
