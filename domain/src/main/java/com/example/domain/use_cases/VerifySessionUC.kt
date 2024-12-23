package com.example.domain.use_cases

import com.example.domain.entities.Result
import com.example.domain.errors.ErrorType
import com.example.domain.errors.Failure
import com.example.domain.repositories.AuthRepository
import com.example.domain.use_cases.base.UseCaseSync
import com.example.domain.utils.Logger
import javax.inject.Inject

class VerifySessionUC @Inject constructor(
    private val logger: Logger,
    private val authRepository: AuthRepository,
): UseCaseSync<Boolean, Unit>() {

    companion object {
        const val TAG = "VerifySessionUC"
    }

    override suspend fun run(params: Unit): Result<Boolean> {
        return try {
            authRepository.thereIsActiveSession()
        } catch (e: Exception) {
            logger.logE(TAG, e)
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }
}