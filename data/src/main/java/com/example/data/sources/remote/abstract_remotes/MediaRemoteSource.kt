package com.example.data.sources.remote.abstract_remotes

import com.example.domain.entities.Result

interface MediaRemoteSource {
    suspend fun getUserImage(): Result<String>
}