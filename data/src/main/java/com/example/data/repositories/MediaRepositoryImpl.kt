package com.example.data.repositories

import android.util.Log
import com.example.data.sources.remote.abstract_remotes.MediaRemoteSource
import com.example.domain.entities.Result
import com.example.domain.errors.ErrorType
import com.example.domain.errors.Failure
import com.example.domain.repositories.MediaRepository
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor (
    private val mediaRemoteSource: MediaRemoteSource
): MediaRepository {
    companion object {
        const val TAG = "MediaRepositoryImpl"
    }

    override suspend fun getUserImage(): Result<String> {
        return try {
            mediaRemoteSource.getUserImage()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }
}