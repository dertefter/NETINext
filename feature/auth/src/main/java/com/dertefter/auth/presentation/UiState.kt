package com.dertefter.auth.presentation

data class UiState(
    val login: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)