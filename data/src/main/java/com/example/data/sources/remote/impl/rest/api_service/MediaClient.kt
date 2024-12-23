package com.example.data.sources.remote.impl.rest.api_service

import com.example.data.sources.remote.impl.rest.data.UserImage
import retrofit2.Response
import retrofit2.http.GET

interface MediaClient {
    @GET("random")
    suspend fun getUserImage(): Response<UserImage>
}