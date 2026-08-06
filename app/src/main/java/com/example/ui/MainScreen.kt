package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.MusicRepository
import com.example.ui.auth.AuthScreen
import com.example.ui.auth.AuthViewModel
import com.example.ui.home.HomeScreen
import com.example.ui.library.LibraryScreen
import com.example.ui.player.ExpandedPlayerScreen
import com.example.ui.player.MiniPlayer
import com.example.ui.player.PlayerViewModel
import com.example.ui.search.SearchScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.theme.NeonGreen

sealed class NavTab(val index: Int, val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector, val tag: String) {
    object Home : NavTab(0, "Home", Icons.Filled.Home, Icons.Outlined.Home, "tab_nav_home")
    object Search : NavTab(1, "Search", Icons.Filled.Search, Icons.Outlined.Search, "tab_nav_search")
    object Library : NavTab(2, "Library", Icons.Filled.LibraryMusic, Icons.Outlined.LibraryMusic, "tab_nav_library")
    object Settings : NavTab(3, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "tab_nav_settings")
}

@Composable
fun MainAppScreen(
    repository: MusicRepository,
    authViewModel: AuthViewModel,
    playerViewModel: PlayerViewModel
) {
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val playerState by playerViewModel.playerState.collectAsStateWithLifecycle()
    val currentUser by repository.currentUser.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(NavTab.Home, NavTab.Search, NavTab.Library, NavTab.Settings)

    if (!authState.isAuthenticated || currentUser == null) {
        // Auth Screen Flow
        AuthScreen(
            viewModel = authViewModel,
            onLoginSuccess = { /* Handled automatically via isAuthenticated state */ }
        )
    } else {
        // Main Dashboard Screen with Persistent Navigation & Mini Player
        Scaffold(
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    // Mini Player Persistent Above Navigation Bar
                    if (playerState.currentSong != null) {
                        MiniPlayer(playerViewModel = playerViewModel)
                    }

                    // Persistent Bottom Navigation Bar with 4 tabs
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .testTag("bottom_navigation_bar")
                    ) {
                        tabs.forEach { tab ->
                            val isSelected = selectedTabIndex == tab.index
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { selectedTabIndex = tab.index },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title,
                                        tint = if (isSelected) NeonGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) NeonGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = NeonGreen.copy(alpha = 0.15f)
                                ),
                                modifier = Modifier.testTag(tab.tag)
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTabIndex) {
                    0 -> HomeScreen(
                        currentUser = currentUser,
                        onSongSelect = { song, queue -> playerViewModel.playSong(song, queue) },
                        onPlaylistSelect = { playlist ->
                            if (playlist.songs.isNotEmpty()) {
                                playerViewModel.playSong(playlist.songs[0], playlist.songs)
                            }
                        }
                    )
                    1 -> SearchScreen(
                        onSongSelect = { song, queue -> playerViewModel.playSong(song, queue) }
                    )
                    2 -> LibraryScreen(
                        repository = repository,
                        onSongSelect = { song, queue -> playerViewModel.playSong(song, queue) },
                        onPlaylistSelect = { playlist ->
                            if (playlist.songs.isNotEmpty()) {
                                playerViewModel.playSong(playlist.songs[0], playlist.songs)
                            }
                        }
                    )
                    3 -> SettingsScreen(
                        repository = repository,
                        onLogout = { authViewModel.logout() }
                    )
                }

                // Fullscreen Expanded Player Overlay
                if (playerState.isExpanded && playerState.currentSong != null) {
                    ExpandedPlayerScreen(playerViewModel = playerViewModel)
                }
            }
        }
    }
}
