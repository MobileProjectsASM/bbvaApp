package com.example.bbvaapp.model

sealed class SessionUiState {
    data object Loading: SessionUiState()
    data class Success(val isActive: Boolean): SessionUiState()
    data object Fail: SessionUiState()
}
