package com.dertefter.auth.presentation

data class UiState(
    val authStatus: AuthStatus,
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)