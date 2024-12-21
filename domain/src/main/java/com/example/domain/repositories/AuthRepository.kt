package com.example.domain.repositories

import com.example.domain.entities.User
import com.example.domain.entities.Result

interface AuthRepository {
    suspend fun loginWithCredentials(userName: String, email: String, password: String): Result<User>
    suspend fun thereIsActiveSession(): Result<Boolean>
}