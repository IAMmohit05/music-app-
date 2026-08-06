package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.AppDatabase
import com.example.data.repository.MusicRepository
import com.example.ui.MainAppScreen
import com.example.ui.auth.AuthViewModel
import com.example.ui.player.PlayerViewModel
import com.example.ui.theme.MusicStreamTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: MusicRepository
    private lateinit var authViewModel: AuthViewModel
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        repository = MusicRepository(database.musicDao())
        authViewModel = AuthViewModel(repository)
        playerViewModel = PlayerViewModel(repository)

        setContent {
            val isDarkTheme by repository.isDarkTheme.collectAsStateWithLifecycle()

            MusicStreamTheme(darkTheme = isDarkTheme) {
                MainAppScreen(
                    repository = repository,
                    authViewModel = authViewModel,
                    playerViewModel = playerViewModel
                )
            }
        }
    }
}
