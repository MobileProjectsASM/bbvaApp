package com.example.bbvaapp.model

import com.example.domain.entities.Gender

sealed class SessionUiState {
    data object Loading: SessionUiState()
    data class Success(val isActive: Boolean): SessionUiState()
    data object Fail: SessionUiState()
}

sealed class SessionInfoUiState {
    data object Loading: SessionInfoUiState()
    data class Success(
        val userId: String,
        val userName: String,
        val userGender: Gender,
        val userAge: Int,
        val userImage: String
    ): SessionInfoUiState()
    data class ErrorToLoadImage(
        val userId: String,
        val userName: String,
        val userGender: Gender,
        val userAge: Int
    ): SessionInfoUiState()
    data class Fail(val sessionInfoError: SessionInfoError): SessionInfoUiState()
}

sealed class SessionInfoError {
    data class ServerError(val code: Int, val description: String): SessionInfoError()
    data object Unknown: SessionInfoError()
}
