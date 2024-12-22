package com.example.data.sources.local.abstract_locals

import com.example.domain.entities.User

interface AuthLocalSource {
    suspend fun saveSession(user: User)
    suspend fun isSessionActive(): Boolean
    suspend fun closeSession()
    suspend fun fetchSession(): User
}