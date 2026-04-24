package com.dertefter.data.common

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val error: AppError) : AppResult<Nothing>()
}

inline fun <T, R> Result<T>.toAppResult(transform: (T) -> R): AppResult<R> =
    fold(
        onSuccess = { AppResult.Success(transform(it)) },
        onFailure = { AppResult.Error(it.toAppError()) }
    )