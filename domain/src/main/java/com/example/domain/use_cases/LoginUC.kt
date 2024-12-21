package com.example.domain.use_cases

import com.example.domain.entities.Result
import com.example.domain.entities.User
import com.example.domain.errors.ErrorType
import com.example.domain.errors.Failure
import com.example.domain.repositories.AuthRepository
import com.example.domain.use_cases.base.UseCaseSync
import com.example.domain.utils.Logger
import javax.inject.Inject

class LoginUC @Inject constructor(
    private val logger: Logger,
    private val authRepository: AuthRepository
) : UseCaseSync<User, LoginUC.Params>() {
    companion object {
        const val TAG = "LoginUC"
    }

    data class Params(
        val userName: String,
        val email: String,
        val password: String
    )

    override suspend fun run(params: Params): Result<User> {
        return try {
            authRepository.loginWithCredentials(params.userName, params.email, params.password)
        } catch(e: Exception) {
            logger.logE(TAG, e)
            Result.Unsuccess(Failure.OtherError(ErrorType.UNKNOWN))
        }
    }
}