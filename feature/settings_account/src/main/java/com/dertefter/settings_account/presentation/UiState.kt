package com.dertefter.settings_account.presentation

import com.dertefter.data.dto.auth.AuthStatus

data class UiState (
    val login: String? = null,
    val ciuAuthStatus: AuthStatus = AuthStatus.Unauthorized,
    val yourNetiAuthStatus: AuthStatus = AuthStatus.Unauthorized,
    val loginHistory: List<String> = emptyList()
)
