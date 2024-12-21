package com.example.data.sources.local.interfaces

interface AuthenticationLocalSource {
    suspend fun saveSession(sessionId: String)
    suspend fun isSessionActive(): Boolean
    suspend fun closeSession()
}