package com.example.domain.entities

import com.example.domain.errors.Failure

sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class Unsuccess<out T>(val failure: Failure): Result<T>()
}
