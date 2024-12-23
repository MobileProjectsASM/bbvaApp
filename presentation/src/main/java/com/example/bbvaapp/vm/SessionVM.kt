package com.example.bbvaapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbvaapp.model.CloseSessionUiState
import com.example.bbvaapp.model.SessionInfoError
import com.example.bbvaapp.model.SessionInfoUiState
import com.example.bbvaapp.model.SessionUiState
import com.example.domain.entities.Result
import com.example.domain.errors.Failure
import com.example.domain.use_cases.CloseSessionUC
import com.example.domain.use_cases.FetchSessionInfoUC
import com.example.domain.use_cases.VerifySessionUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionVM @Inject constructor(
    private val verifySessionUC: VerifySessionUC,
    private val fetchSessionInfoUC: FetchSessionInfoUC,
    private val closeSessionUC: CloseSessionUC,
): ViewModel() {
    companion object {
        const val TAG = "SessionViewModel"
    }

    private val _sessionState: MutableStateFlow<SessionUiState> = MutableStateFlow(SessionUiState.Loading)
    private val _sessionInfoState: MutableStateFlow<SessionInfoUiState?> = MutableStateFlow(null)
    private val _closeSessionState: MutableStateFlow<CloseSessionUiState?> = MutableStateFlow(null)

    val sessionState: StateFlow<SessionUiState> = _sessionState
    val sessionInfoState: StateFlow<SessionInfoUiState?> = _sessionInfoState
    val closeSessionState: StateFlow<CloseSessionUiState?> = _closeSessionState

    fun verifySessionActive() {
        viewModelScope.launch {
            _sessionState.update { SessionUiState.Loading }
            when (val result = verifySessionUC.execute(params = Unit)) {
                is Result.Success -> _sessionState.update {
                    SessionUiState.Success(result.data)
                }
                is Result.Unsuccess -> SessionUiState.Fail
            }
        }
    }

    fun resetSessionInfoState() {
        _sessionInfoState.update { null }
    }

    fun fetchSessionInfo() {
        viewModelScope.launch {
            _sessionInfoState.update {
                SessionInfoUiState.Loading
            }
            val sessionInfoResult = fetchSessionInfoUC.execute(params = Unit)
            _sessionInfoState.update {
                when (sessionInfoResult) {
                    is Result.Success -> {
                        val sessionInfo = sessionInfoResult.data
                        if (sessionInfo.userImage == null) {
                            SessionInfoUiState.ErrorToLoadImage(
                                userId = sessionInfo.userId,
                                userName = sessionInfo.userName,
                                userGender = sessionInfo.userGender,
                                userAge = sessionInfo.userAge
                            )
                        } else {
                            SessionInfoUiState.Success(
                                userId = sessionInfo.userId,
                                userName = sessionInfo.userName,
                                userGender = sessionInfo.userGender,
                                userAge = sessionInfo.userAge,
                                userImage = sessionInfo.userImage!!
                            )
                        }
                    }
                    is Result.Unsuccess -> when (val failure = sessionInfoResult.failure) {
                        is Failure.OtherError -> SessionInfoUiState.Fail(SessionInfoError.Unknown)
                        is Failure.ServerError -> SessionInfoUiState.Fail(SessionInfoError.ServerError(failure.code, failure.description))
                    }
                }
            }
        }
    }

    fun resetCloseSessionState() {
        _closeSessionState.update { null }
    }

    fun logout(userId: String) {
        viewModelScope.launch {
            _closeSessionState.update {
                CloseSessionUiState.Loading
            }
            delay(2000)
            val closeSessionResult = closeSessionUC.execute(params = Unit)
            val closeSessionState = when (closeSessionResult) {
                is Result.Success -> CloseSessionUiState.Success(userId)
                is Result.Unsuccess -> CloseSessionUiState.Fail
            }
            _closeSessionState.update {
                closeSessionState
            }
        }
    }
}