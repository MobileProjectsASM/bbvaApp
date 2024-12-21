package com.example.domain.use_cases

import com.example.domain.entities.Result
import com.example.domain.errors.ErrorType
import com.example.domain.errors.Failure
import com.example.domain.repositories.AuthRepository
import com.example.domain.use_cases.base.UseCaseSync
import com.example.domain.utils.Completed
import com.example.domain.utils.Logger
import javax.inject.Inject

class CloseSessionUC @Inject constructor(
    private var logger: Logger,
    private var authRepository: AuthRepository
): UseCaseSync<Completed, Unit>() {

    companion object {
        const val TAG = "CloseSessionUC"
    }

    override suspend fun run(params: Unit): Result<Completed> {
        return try {
            authRepository.closeSession()
        } catch (e: Exception) {
            logger.logE(TAG, e)
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }
}