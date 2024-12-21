package com.example.data.sources.remote.interfaces

import com.example.domain.entities.User

interface AuthenticationRemoteSource {
    suspend fun login(username: String, email: String, password: String): Result<User>
}