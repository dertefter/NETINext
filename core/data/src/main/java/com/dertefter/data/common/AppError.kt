package com.dertefter.data.common

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

sealed interface AppError {

    sealed interface Network : AppError {
        data object NoInternet : Network
        data object Timeout : Network

        data object Unauthorized : Network

    }

    data object Unknown : AppError
}

fun Throwable.toAppError(): AppError {
    return when (this) {
        is AppException -> this.error
        is AppError -> this
        is SocketTimeoutException -> AppError.Network.Timeout
        is IOException -> AppError.Network.NoInternet
        else -> AppError.Unknown
    }
}