package com.example.bbvaapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbvaapp.model.InputEmailError
import com.example.bbvaapp.model.InputPasswordError
import com.example.bbvaapp.model.InputState
import com.example.bbvaapp.model.InputUiState
import com.example.bbvaapp.model.LoginError
import com.example.bbvaapp.model.LoginFormUiState
import com.example.bbvaapp.model.LoginUiState
import com.example.domain.entities.Result
import com.example.domain.errors.Failure
import com.example.domain.use_cases.LoginUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val loginUC: LoginUC
): ViewModel() {
    private val emailRegex = "[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}".toRegex()

    private val _loginFormUiState = MutableStateFlow(LoginFormUiState(emailUiState = InputUiState(), passwordUiState = InputUiState()))
    private val _loginState = MutableStateFlow<LoginUiState?>(null)

    val loginFormUiState: StateFlow<LoginFormUiState> = _loginFormUiState
    val loginState: StateFlow<LoginUiState?> = _loginState

    fun resetLoginState() {
        _loginState.update { null }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.update { LoginUiState.Loading }
            val result = loginUC.execute(params = LoginUC.Params("", email, password))
            _loginState.update {
                when (result) {
                    is Result.Success -> {
                        LoginUiState.Success(result.data.userId)
                    }
                    is Result.Unsuccess -> when (val failure = result.failure) {
                        is Failure.OtherError -> LoginUiState.Failure(LoginError.Unknown)
                        is Failure.ServerError -> LoginUiState.Failure(LoginError.ServerError(failure.code, failure.description))
                    }
                }
            }
        }
    }

    fun validateLoginForm(email: String, password: String) {
        val errorsEmail = validateEmail(email)
        val errorsPassword = validatePassword(password)
        _loginFormUiState.update {
            val emailUiState = errorsEmail.run {
                if (isEmpty()) InputUiState(email, InputState.Success)
                else InputUiState(email, InputState.Error(errorsEmail))
            }
            val passwordUiState = errorsPassword.run {
                if (isEmpty()) InputUiState(password, InputState.Success)
                else InputUiState(password, InputState.Error(errorsPassword))
            }
            it.copy(
                emailUiState = emailUiState,
                passwordUiState = passwordUiState,
            )
        }
    }

    private fun validateEmail(email: String): List<InputEmailError> {
        val errors = mutableListOf<InputEmailError>()
        if (email.isEmpty()) errors.add(InputEmailError.EMPTY)
        if (!emailRegex.matches(email)) errors.add(InputEmailError.INVALID_EMAIL)
        return errors
    }

    private fun validatePassword(password: String): List<InputPasswordError> {
        val errors = mutableListOf<InputPasswordError>()
        if (password.isEmpty()) errors.add(InputPasswordError.EMPTY)
        if (password.count() < 8) errors.add(InputPasswordError.LEAST_THAN_8_CHARACTERS)
        if (!password.contains("[A-Z]".toRegex())) errors.add(InputPasswordError.LEAST_ONE_UPPERCASE)
        if (!password.contains("\\d".toRegex())) errors.add(InputPasswordError.LEAST_ONE_NUMBER)
        if (!password.contains("[@$!%*?&#]".toRegex())) errors.add(InputPasswordError.LEAST_ONE_SPECIAL_CHARACTER)
        return errors
    }
}