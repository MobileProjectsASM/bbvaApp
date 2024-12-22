package com.example.data.sources.remote.abstract_remotes

import com.example.domain.entities.Result
import com.example.domain.entities.User

interface AuthRemoteSource {
    suspend fun login(username: String, email: String, password: String): Result<User>
}