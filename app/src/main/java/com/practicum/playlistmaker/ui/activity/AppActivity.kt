package com.practicum.playlistmaker.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.practicum.playlistmaker.creator.Storage
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.TracksRepositoryImpl
import com.practicum.playlistmaker.ui.materialTheme.PlaylistMakerTheme
import com.practicum.playlistmaker.ui.navigation.PlaylistNavHost



class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val isDark = rememberSaveable { mutableStateOf(false) }

            val storage = remember { Storage() }
            val networkClient = remember { RetrofitNetworkClient(storage) }
            val repository = remember { TracksRepositoryImpl(networkClient) }

            PlaylistMakerTheme(darkTheme = isDark.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlaylistNavHost(
                        repository = repository,
                        isDarkTheme = isDark.value,
                        onToggleTheme = { isDark.value = it }
                    )
                }
            }
        }
    }
}
