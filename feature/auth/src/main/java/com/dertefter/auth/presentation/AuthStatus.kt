package com.dertefter.auth.presentation

sealed interface AuthStatus {
    data object Unauthorized : AuthStatus
    data object Loading : AuthStatus
    data object Authorized : AuthStatus
}