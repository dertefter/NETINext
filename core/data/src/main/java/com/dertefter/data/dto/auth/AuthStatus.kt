package com.dertefter.data.dto.auth

import com.dertefter.data.common.AppError

sealed interface AuthStatus {

    val login: String? get() = null

    object Unauthorized : AuthStatus

    data class Loading (
        override val login: String
    ) : AuthStatus

    data class Authorized(
        override val login: String
    ): AuthStatus

    data class Error(
        override val login: String,
        val error: AppError? = null
    ): AuthStatus

}