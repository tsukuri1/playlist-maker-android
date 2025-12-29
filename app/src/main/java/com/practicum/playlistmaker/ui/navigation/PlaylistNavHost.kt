package com.practicum.playlistmaker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practicum.playlistmaker.domain.TracksRepository
import com.practicum.playlistmaker.ui.favorites.FavoritesScreen
import com.practicum.playlistmaker.ui.main.MainScreen
import com.practicum.playlistmaker.ui.playlists.PlaylistsScreen
import com.practicum.playlistmaker.ui.search.SearchScreen
import com.practicum.playlistmaker.ui.search.SearchViewModel
import com.practicum.playlistmaker.ui.search.SearchViewModelFactory
import com.practicum.playlistmaker.ui.settings.SettingsScreen

@Composable
fun PlaylistNavHost(
    repository: TracksRepository,
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MAIN.name) {

        composable(Screen.MAIN.name) {
            MainScreen(
                onSearchClick = { navController.navigate(Screen.SEARCH.name) },
                onPlaylistsClick = { navController.navigate(Screen.PLAYLISTS.name) },
                onFavoritesClick = { navController.navigate(Screen.FAVORITES.name) },
                onSettingsClick = { navController.navigate(Screen.SETTINGS.name) },
                isDarkTheme = isDarkTheme
            )
        }

        composable(Screen.SEARCH.name) {
            val factory = SearchViewModelFactory(repository)
            val vm: SearchViewModel = viewModel(factory = factory)

            SearchScreen(
                viewModel = vm,
                isDarkTheme = isDarkTheme,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.SETTINGS.name) {
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onShareClick = {},
                onSupportClick = {},
                onAgreementClick = {},
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.PLAYLISTS.name) {
            PlaylistsScreen(
                onBackClick = { navController.popBackStack() },
                isDarkTheme = isDarkTheme
            )
        }

        composable(Screen.FAVORITES.name) {
            FavoritesScreen(onBackClick = { navController.popBackStack() })
        }
    }
}