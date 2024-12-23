package com.example.domain.use_cases

import com.example.domain.entities.Result
import com.example.domain.entities.User
import com.example.domain.errors.ErrorType
import com.example.domain.errors.Failure
import com.example.domain.repositories.AuthRepository
import com.example.domain.repositories.MediaRepository
import com.example.domain.use_cases.base.UseCaseSync
import com.example.domain.utils.Logger
import javax.inject.Inject

class FetchSessionInfoUC @Inject constructor(
    private val logger: Logger,
    private val authRepository: AuthRepository,
    private val mediaRepository: MediaRepository
): UseCaseSync<User, Unit>() {
    companion object {
        const val TAG = "FetchUserUC"
    }

    override suspend fun run(params: Unit): Result<User> {
        return try {
            val userResult = authRepository.fetchSession()
            if (userResult is Result.Unsuccess) return userResult
            val user = (userResult as Result.Success).data
            val userImageResult = mediaRepository.getUserImage()
            if (userImageResult is Result.Unsuccess) return Result.Unsuccess(userImageResult.failure)
            val userImage = (userImageResult as Result.Success).data
            Result.Success(user.copy(userImage = userImage))
        } catch (e: Exception) {
            logger.logE(TAG, e)
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }
}