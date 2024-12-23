package com.example.data.sources.hardware

interface ConnectionSource {
    suspend fun isNetworkAvailable(): Boolean
}