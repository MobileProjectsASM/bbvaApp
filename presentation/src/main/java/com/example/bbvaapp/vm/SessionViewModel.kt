package com.example.bbvaapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbvaapp.model.SessionUiState
import com.example.domain.entities.Result
import com.example.domain.use_cases.VerifySessionUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val verifySessionUC: VerifySessionUC
): ViewModel() {
    companion object {
        const val TAG = "SessionViewModel"
    }

    private val _sessionState: MutableStateFlow<SessionUiState> = MutableStateFlow(SessionUiState.Loading)

    val sessionState: StateFlow<SessionUiState> = _sessionState

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
}