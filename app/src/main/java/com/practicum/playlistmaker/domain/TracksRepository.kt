package com.practicum.playlistmaker.domain

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
    suspend fun getAllTracks(): List<Track>
}
