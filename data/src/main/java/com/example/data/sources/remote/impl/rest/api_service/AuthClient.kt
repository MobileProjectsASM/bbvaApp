package com.example.data.sources.remote.impl.rest.api_service

import com.example.data.sources.remote.impl.rest.data.AuthData
import com.example.data.sources.remote.impl.rest.data.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthClient {
    @POST("login")
    suspend fun login(@Body authData: AuthData): Response<UserData>
}