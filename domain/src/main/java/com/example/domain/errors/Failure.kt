package com.example.domain.errors

sealed class Failure {
    data class ServerError(
        val code: Int,
        val description: String
    ) : Failure()
    data class OtherError(
        val errorType: ErrorType
    ) : Failure()
}

enum class ErrorType {
    CONNECTION,
    UNKNOWN
}