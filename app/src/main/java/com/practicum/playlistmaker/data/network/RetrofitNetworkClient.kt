package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.creator.Storage

class RetrofitNetworkClient(
    private val storage: Storage = Storage()
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Any {
        return when (dto) {
            is TracksSearchRequest -> {
                val results = storage.search(dto.expression)

                val response = TracksSearchResponse(results)
                response.resultCode = 200
                response
            }

            else -> {
                val response = TracksSearchResponse(emptyList())
                response.resultCode = 400
                response
            }
        }
    }
}
