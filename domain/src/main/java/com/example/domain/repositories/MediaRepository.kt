package com.example.domain.repositories

import com.example.domain.entities.Result

interface MediaRepository {
    suspend fun getUserImage(): Result<String>
}