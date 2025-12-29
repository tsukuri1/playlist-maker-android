package com.practicum.playlistmaker.data.network

interface NetworkClient {
    suspend fun doRequest(dto: Any): Any
}