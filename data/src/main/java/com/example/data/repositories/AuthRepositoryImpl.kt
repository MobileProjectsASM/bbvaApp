package com.example.data.repositories

import android.util.Log
import com.example.data.sources.hardware.ConnectionSource
import com.example.data.sources.local.abstract_locals.AuthLocalSource
import com.example.data.sources.remote.abstract_remotes.AuthRemoteSource
import com.example.domain.entities.Result
import com.example.domain.entities.User
import com.example.domain.errors.ErrorType
import com.example.domain.errors.Failure
import com.example.domain.repositories.AuthRepository
import com.example.domain.utils.Completed
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteSource: AuthRemoteSource,
    private val authLocalSource: AuthLocalSource,
    private val connectionSource: ConnectionSource
) : AuthRepository {

    companion object {
        const val TAG = "AuthRepositoryImpl"
    }

    override suspend fun loginWithCredentials(
        userName: String,
        email: String,
        password: String
    ): Result<User> {
        return try {
            val thereIsConnection = connectionSource.isNetworkAvailable()
            if (!thereIsConnection) return Result.Unsuccess(Failure.OtherError(ErrorType.CONNECTION))
            authRemoteSource.login(userName, email, password)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }

    override suspend fun thereIsActiveSession(): Result<Boolean> {
        return try {
            val isSessionActive = authLocalSource.isSessionActive()
            Result.Success(isSessionActive)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }

    override suspend fun saveSession(user: User): Result<Completed> {
        return try {
            authLocalSource.saveSession(user)
            Result.Success(Completed)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }

    override suspend fun closeSession(): Result<Completed> {
        return try {
            authLocalSource.closeSession()
            Result.Success(Completed)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }

    override suspend fun fetchSession(): Result<User> {
        return try {
            val user = authLocalSource.fetchSession()
            Result.Success(user)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }
}