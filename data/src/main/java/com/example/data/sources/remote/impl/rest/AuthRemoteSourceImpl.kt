package com.example.data.sources.remote.impl.rest

import android.util.Log
import com.example.data.sources.remote.abstract_remotes.AuthRemoteSource
import com.example.data.sources.remote.impl.rest.api_service.AuthClient
import com.example.data.sources.remote.impl.rest.data.AuthData
import com.example.data.sources.remote.impl.rest.data.AuthError
import com.example.data.sources.remote.impl.rest.mappers.UserMapper
import com.example.domain.entities.Result
import com.example.domain.entities.User
import com.example.domain.errors.ErrorType
import com.example.domain.errors.Failure
import com.google.gson.Gson
import javax.inject.Inject

class AuthRemoteSourceImpl @Inject constructor(
    private val authClient: AuthClient,
    private val userMapper: UserMapper,
    private val gson: Gson,
): AuthRemoteSource {
    companion object {
        const val TAG = "AuthRemoteSourceImpl"
    }

    override suspend fun login(username: String, email: String, password: String): Result<User> {
        return try {
            val response = authClient.login(AuthData(username, email, password))
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                if (errorBody.isNullOrBlank()) {
                    throw Exception("Error body is null or blank")
                }
                val apiError = gson.fromJson(errorBody, AuthError::class.java)
                val codeInt = apiError.code.toIntOrNull() ?: 500
                return Result.Unsuccess(Failure.ServerError(codeInt, apiError.message))
            }
            val userData = response.body() ?: throw Exception("body is null")
            val user = userMapper.userDataToUser(userData)
            Result.Success(user)
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }
}