package com.example.data.sources.remote.impl.rest

import android.util.Log
import com.example.data.sources.remote.abstract_remotes.MediaRemoteSource
import com.example.data.sources.remote.impl.rest.api_service.MediaClient
import com.example.data.sources.remote.impl.rest.data.UserImageError
import com.example.data.sources.remote.impl.rest.mappers.MediaMapper
import com.example.domain.entities.Result
import com.example.domain.errors.ErrorType
import com.example.domain.errors.Failure
import com.google.gson.Gson
import javax.inject.Inject

class MediaRemoteSourceImpl @Inject constructor(
    private val mediaClient: MediaClient,
    private val mediaMapper: MediaMapper,
    private val gson: Gson,
): MediaRemoteSource {
    companion object {
        const val TAG = "MediaRemoteSourceImpl"
    }

    override suspend fun getUserImage(): Result<String> {
        return try {
            val response = mediaClient.getUserImage()
            if (!response.isSuccessful) {
                val errorBody = response.errorBody() ?: throw Exception("Error Body is empty")
                val error = gson.fromJson(errorBody.string(), UserImageError::class.java)
                val codeInt = error.code.toIntOrNull() ?: 500
                return Result.Unsuccess(Failure.ServerError(codeInt, error.message))
            }
            val data = response.body() ?: throw Exception("Body is null")
            val userImage = mediaMapper.userImageToDomain(data)
            Result.Success(userImage)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }
}