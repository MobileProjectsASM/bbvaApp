package com.example.data.sources.remote.interfaces

import com.example.domain.entities.User

interface AuthRemoteSource {
    suspend fun login(username: String, email: String, password: String): User
}