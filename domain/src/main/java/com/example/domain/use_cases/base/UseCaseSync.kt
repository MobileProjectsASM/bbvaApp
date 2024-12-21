package com.example.domain.use_cases.base

import com.example.domain.entities.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class UseCaseSync<out Type, in Params> where Type : Any {
    abstract suspend fun run(params: Params): com.example.domain.entities.Result<Type>

    suspend fun execute(
        coroutineContext: CoroutineContext = Dispatchers.IO,
        params: Params
    ): Result<Type> = withContext(coroutineContext) {
        run(params)
    }

}