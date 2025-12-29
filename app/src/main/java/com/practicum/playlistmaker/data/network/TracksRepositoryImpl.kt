package com.practicum.playlistmaker.data.network
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.domain.TracksRepository
import kotlinx.coroutines.delay
class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {
    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        delay(500)
        if (response is TracksSearchResponse && response.resultCode == 200) {
            return response.results.map { dto ->
                Track(dto.trackName, dto.artistName, dto.trackTimeMillis)
            }
        }
        return emptyList()
    }
    override suspend fun getAllTracks(): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(""))
        delay(500)
        if (response is TracksSearchResponse && response.resultCode == 200) {
            return response.results.map { dto ->
                Track(dto.trackName, dto.artistName, dto.trackTimeMillis)
            }
        }
        return emptyList()
    }
}