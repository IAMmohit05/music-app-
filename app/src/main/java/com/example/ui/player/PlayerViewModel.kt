package com.example.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.MockData
import com.example.data.model.RepeatMode
import com.example.data.model.Song
import com.example.data.repository.MusicRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PlayerState(
    val currentSong: Song? = null,
    val queue: List<Song> = MockData.sampleSongs,
    val currentIndex: Int = 0,
    val isPlaying: Boolean = false,
    val currentPositionSeconds: Int = 0,
    val isShuffle: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val isExpanded: Boolean = false,
    val showLyrics: Boolean = false
)

class PlayerViewModel(private val repository: MusicRepository) : ViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    // Observe whether current song is liked from Room DB
    val isCurrentSongLiked: StateFlow<Boolean> = combine(
        _playerState,
        repository.likedSongs
    ) { state, likedList ->
        val currentId = state.currentSong?.id ?: return@combine false
        likedList.any { it.id == currentId }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private var playbackJob: Job? = null

    init {
        // Default initial song loaded
        if (MockData.sampleSongs.isNotEmpty()) {
            _playerState.value = _playerState.value.copy(
                currentSong = MockData.sampleSongs[0],
                queue = MockData.sampleSongs,
                currentIndex = 0
            )
        }
    }

    fun playSong(song: Song, newQueue: List<Song> = MockData.sampleSongs) {
        val index = newQueue.indexOfFirst { it.id == song.id }.let { if (it == -1) 0 else it }
        _playerState.value = _playerState.value.copy(
            currentSong = song,
            queue = newQueue,
            currentIndex = index,
            isPlaying = true,
            currentPositionSeconds = 0
        )
        startPlaybackTimer()
    }

    fun togglePlayPause() {
        val currentState = _playerState.value
        if (currentState.currentSong == null && currentState.queue.isNotEmpty()) {
            playSong(currentState.queue[0])
            return
        }

        val newIsPlaying = !currentState.isPlaying
        _playerState.value = currentState.copy(isPlaying = newIsPlaying)

        if (newIsPlaying) {
            startPlaybackTimer()
        } else {
            playbackJob?.cancel()
        }
    }

    fun seekTo(seconds: Int) {
        val songDuration = _playerState.value.currentSong?.durationSeconds ?: 0
        val clamped = seconds.coerceIn(0, songDuration)
        _playerState.value = _playerState.value.copy(currentPositionSeconds = clamped)
    }

    fun playNext() {
        val state = _playerState.value
        if (state.queue.isEmpty()) return

        val nextIndex = when {
            state.isShuffle -> (state.queue.indices).random()
            state.repeatMode == RepeatMode.REPEAT_ONE -> state.currentIndex
            else -> (state.currentIndex + 1) % state.queue.size
        }

        val nextSong = state.queue[nextIndex]
        _playerState.value = state.copy(
            currentSong = nextSong,
            currentIndex = nextIndex,
            currentPositionSeconds = 0,
            isPlaying = true
        )
        startPlaybackTimer()
    }

    fun playPrevious() {
        val state = _playerState.value
        if (state.queue.isEmpty()) return

        // If played more than 3 seconds, restart current song
        if (state.currentPositionSeconds > 3) {
            seekTo(0)
            return
        }

        val prevIndex = if (state.currentIndex - 1 < 0) state.queue.size - 1 else state.currentIndex - 1
        val prevSong = state.queue[prevIndex]

        _playerState.value = state.copy(
            currentSong = prevSong,
            currentIndex = prevIndex,
            currentPositionSeconds = 0,
            isPlaying = true
        )
        startPlaybackTimer()
    }

    fun toggleShuffle() {
        val current = _playerState.value.isShuffle
        _playerState.value = _playerState.value.copy(isShuffle = !current)
    }

    fun cycleRepeatMode() {
        val current = _playerState.value.repeatMode
        val nextMode = when (current) {
            RepeatMode.NONE -> RepeatMode.REPEAT_ALL
            RepeatMode.REPEAT_ALL -> RepeatMode.REPEAT_ONE
            RepeatMode.REPEAT_ONE -> RepeatMode.NONE
        }
        _playerState.value = _playerState.value.copy(repeatMode = nextMode)
    }

    fun toggleFavoriteCurrentSong() {
        val song = _playerState.value.currentSong ?: return
        viewModelScope.launch {
            repository.toggleLikeSong(song)
        }
    }

    fun setExpanded(expanded: Boolean) {
        _playerState.value = _playerState.value.copy(isExpanded = expanded)
    }

    fun toggleShowLyrics() {
        val current = _playerState.value.showLyrics
        _playerState.value = _playerState.value.copy(showLyrics = !current)
    }

    private fun startPlaybackTimer() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (_playerState.value.isPlaying) {
                delay(1000L)
                val state = _playerState.value
                val duration = state.currentSong?.durationSeconds ?: 200
                if (state.currentPositionSeconds < duration) {
                    _playerState.value = state.copy(currentPositionSeconds = state.currentPositionSeconds + 1)
                } else {
                    // Song finished
                    if (state.repeatMode == RepeatMode.REPEAT_ONE) {
                        seekTo(0)
                    } else {
                        playNext()
                    }
                }
            }
        }
    }
}
