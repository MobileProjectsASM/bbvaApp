package com.example.bbvaapp.model

sealed class LoginUiState {
    data object Loading: LoginUiState()
    data class Success(val userId: String): LoginUiState()
    data class Failure(val loginError: LoginError): LoginUiState()
}

data class LoginFormUiState(
    val emailUiState: InputUiState<InputEmailError>,
    val passwordUiState: InputUiState<InputPasswordError>
)

sealed class InputState<out InputError> {
    data object Init: InputState<Nothing>()
    data object Success: InputState<Nothing>()
    data class Error<out InputError>(
        val errors: List<InputError>
    ): InputState<InputError>()
}

data class InputUiState<out InputError>(
    val value: String = "",
    val state: InputState<InputError> = InputState.Init
)

enum class InputEmailError {
    EMPTY,
    INVALID_EMAIL
}

enum class InputPasswordError {
    EMPTY,
    LEAST_THAN_8_CHARACTERS,
    LEAST_ONE_NUMBER,
    LEAST_ONE_SPECIAL_CHARACTER,
    LEAST_ONE_UPPERCASE,
}

sealed class LoginError {
    data class ServerError(val code: Int, val description: String): LoginError()
    data object Unknown: LoginError()
}