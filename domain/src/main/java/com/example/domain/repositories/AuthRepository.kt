package com.example.domain.repositories

import com.example.domain.entities.User
import com.example.domain.entities.Result
import com.example.domain.utils.Completed

interface AuthRepository {
    suspend fun loginWithCredentials(userName: String, email: String, password: String): Result<User>
    suspend fun thereIsActiveSession(): Result<Boolean>
    suspend fun saveSession(sessionId: User): Result<Completed>
    suspend fun closeSession(): Result<Completed>
}