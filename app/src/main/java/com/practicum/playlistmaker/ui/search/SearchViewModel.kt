package com.practicum.playlistmaker.ui.search
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.domain.TracksRepository
import com.practicum.playlistmaker.presentation.AppTrack
import com.practicum.playlistmaker.presentation.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class SearchViewModel(
    private val repository: TracksRepository
) : ViewModel() {
    private val _query = mutableStateOf("")
    val query: State<String> get() = _query
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isEmpty()) {
            setState(SearchState.Empty)
        }
    }
    fun clear() {
        _query.value = ""
        setState(SearchState.Empty)
    }
    private val _state = mutableStateOf<SearchState>(SearchState.Empty)
    val state: State<SearchState> get() = _state
    private fun setState(newState: SearchState) {
        _state.value = newState
    }
    fun search() {
        val q = _query.value.trim()
        if (q.isEmpty()) {
            setState(SearchState.Empty)
            return
        }
        viewModelScope.launch {
            setState(SearchState.Loading)
            val list: List<Track> = withContext(Dispatchers.IO) {
                repository.searchTracks(q)
            }
            val appTracks = list.map { track ->
                val seconds = track.trackTimeMillis / 1000
                val minutes = seconds / 60
                val trackTime = "%d:%02d".format(minutes, seconds % 60)
                AppTrack(track.trackName, track.artistName, trackTime)
            }
            setState(
                if (appTracks.isNotEmpty()) SearchState.Content(appTracks)
                else SearchState.Error
            )
        }
    }
}