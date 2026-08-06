package com.example.data.model

object MockData {

    val sampleSongs = listOf(
        Song(
            id = "s1",
            title = "Midnight Horizon",
            artist = "Aetherium & Neon Pulse",
            album = "Cyber Dreams",
            durationSeconds = 214,
            albumArtUrl = "https://picsum.photos/id/1025/500/500",
            genre = "Synthwave",
            isTrending = true,
            isTopChart = true,
            lyrics = """
                [Verse 1]
                Riding through the neon street lights
                Shadows fading in the cool night
                Synth bass beating like a heart rate
                We're escaping from our own fate

                [Chorus]
                Chasing the midnight horizon
                Where the electric stars are rising
                Hold on to the frequency tonight
                Lost in the neon crimson light

                [Verse 2]
                Retro echoes on the radio
                Fading out into the slow glow
                Never looking back again
                Until the darkness comes to end
            """.trimIndent()
        ),
        Song(
            id = "s2",
            title = "Kesariya Vibes",
            artist = "Arijit & Pritam",
            album = "Bollywood Chill Hits",
            durationSeconds = 268,
            albumArtUrl = "https://picsum.photos/id/1062/500/500",
            genre = "Bollywood",
            isTrending = true,
            isTopChart = true,
            lyrics = """
                [Verse 1]
                Mujhko kitna pyar hai tumse
                Poochho is dil ki dhadkan se
                Saffar ye suhana ho gaya
                Jabse tera deewana ho gaya

                [Chorus]
                Kesariya tera ishq hai piya
                Rang jaaun jo main haath lagaun
                Din beete tere khayal mein
                Raat kate tere sapno mein
            """.trimIndent()
        ),
        Song(
            id = "s3",
            title = "Lo-Fi Study Rain",
            artist = "ChillHop Cafe",
            album = "Coffee & Rain Beats",
            durationSeconds = 185,
            albumArtUrl = "https://picsum.photos/id/1069/500/500",
            genre = "Lo-Fi",
            isTrending = false,
            isTopChart = true,
            lyrics = "[Instrumental Lo-Fi Chill Beats]"
        ),
        Song(
            id = "s4",
            title = "Starry Nights & Echoes",
            artist = "Luna Bay",
            album = "Celestial Waves",
            durationSeconds = 205,
            albumArtUrl = "https://picsum.photos/id/1015/500/500",
            genre = "Pop",
            isTrending = true,
            isTopChart = false,
            lyrics = """
                [Verse 1]
                Look up at the galaxy tonight
                Counting every single shining light
                Whisper promises into the breeze
                Sailing across the quiet seas

                [Chorus]
                Starry nights and golden memories
                Writing our own sweet melodies
            """.trimIndent()
        ),
        Song(
            id = "s5",
            title = "Bhangra Groove",
            artist = "Diljit & Badshah",
            album = "Urban Punjabi",
            durationSeconds = 198,
            albumArtUrl = "https://picsum.photos/id/1084/500/500",
            genre = "Punjabi",
            isTrending = true,
            isTopChart = true,
            lyrics = """
                [Verse 1]
                Nachde saare Gabru aaj
                Bass dholak da bajaaj
                Soniye tu vi aaja naal
                Kar lai thoda jeha kamaal
            """.trimIndent()
        ),
        Song(
            id = "s6",
            title = "Electric Storm",
            artist = "DJ Kairo",
            album = "Tomorrowland Anthem",
            durationSeconds = 240,
            albumArtUrl = "https://picsum.photos/id/1074/500/500",
            genre = "EDM",
            isTrending = false,
            isTopChart = true,
            lyrics = "[EDM Drop & High Energy Synth Build Up]"
        ),
        Song(
            id = "s7",
            title = "Acoustic Sunset",
            artist = "Oliver Vance",
            album = "Unplugged Journeys",
            durationSeconds = 222,
            albumArtUrl = "https://picsum.photos/id/1039/500/500",
            genre = "Acoustic",
            isTrending = false,
            isTopChart = false,
            lyrics = """
                [Verse 1]
                Gentle strumming on a wooden guitar
                Watching the orange sun fall far
                Simplicity in every chord
                Grateful for the peace restored
            """.trimIndent()
        ),
        Song(
            id = "s8",
            title = "Velvet Saxophone",
            artist = "The Miles Quartet",
            album = "Midnight Jazz Club",
            durationSeconds = 310,
            albumArtUrl = "https://picsum.photos/id/1082/500/500",
            genre = "Jazz",
            isTrending = false,
            isTopChart = false,
            lyrics = "[Smooth Jazz Instrumental Performance]"
        )
    )

    val genreCategories = listOf(
        GenreCategory("g1", "Pop & Dance", "0xFFE91E63", "MusicNote"),
        GenreCategory("g2", "Bollywood Hits", "0xFFFF9800", "MusicNote"),
        GenreCategory("g3", "Punjabi Beats", "0xFF4CAF50", "MusicNote"),
        GenreCategory("g4", "Lo-Fi & Chill", "0xFF9C27B0", "MusicNote"),
        GenreCategory("g5", "EDM & Festival", "0xFF00BCD4", "MusicNote"),
        GenreCategory("g6", "Rock & Metal", "0xFFF44336", "MusicNote"),
        GenreCategory("g7", "Classical Harmony", "0xFF795548", "MusicNote"),
        GenreCategory("g8", "Hip-Hop & Rap", "0xFF3F51B5", "MusicNote")
    )

    val featuredPlaylists = listOf(
        Playlist(
            id = "p1",
            name = "Top 50 Global Hits",
            description = "The hottest tracks around the world updated daily",
            coverUrl = "https://picsum.photos/id/1040/500/500",
            songs = sampleSongs.filter { it.isTopChart }
        ),
        Playlist(
            id = "p2",
            name = "Desi Bollywood Romance",
            description = "Soft romantic melodies for your soul",
            coverUrl = "https://picsum.photos/id/1062/500/500",
            songs = sampleSongs.filter { it.genre == "Bollywood" || it.genre == "Punjabi" }
        ),
        Playlist(
            id = "p3",
            name = "Late Night Synthwave",
            description = "Retro synth & neon aesthetics for night rides",
            coverUrl = "https://picsum.photos/id/1025/500/500",
            songs = sampleSongs.filter { it.genre == "Synthwave" || it.genre == "EDM" }
        ),
        Playlist(
            id = "p4",
            name = "Lo-Fi Study Session",
            description = "Deep focus beats and soothing rain sounds",
            coverUrl = "https://picsum.photos/id/1069/500/500",
            songs = sampleSongs.filter { it.genre == "Lo-Fi" || it.genre == "Acoustic" }
        )
    )
}
