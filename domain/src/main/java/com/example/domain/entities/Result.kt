package com.example.domain.entities

import com.example.domain.errors.Failure

sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class Unsuccess<out T>(val failure: Failure): Result<T>()

    val isSuccess get() = this is Success<T>
    val isUnsuccess get() = this is Unsuccess<T>

}

fun <T, S> Result.Unsuccess<T>.toFailure(): Result.Unsuccess<S> = Result.Unsuccess(this.failure)
fun <T> Result<T>.asSuccess(): Result.Success<T> = this as Result.Success<T>
fun <T> Result<T>.asFailure(): Result.Unsuccess<T> = this as Result.Unsuccess<T>
fun <T> Failure.toFailure(): Result.Unsuccess<T> = Result.Unsuccess(this)
fun <T> T.toSuccessful(): Result.Success<T> = Result.Success(this)