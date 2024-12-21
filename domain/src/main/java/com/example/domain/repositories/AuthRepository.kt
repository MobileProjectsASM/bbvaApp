package com.example.domain.repositories

interface AuthRepository {
    suspend fun thereIsActiveSession(): Result<Boolean>
}