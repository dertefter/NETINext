package com.dertefter.profile.presentation

import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.data.dto.user.LksDto
import com.dertefter.navigation.Routes

data class UiState(
    val authStatus: AuthStatus,
    val userInfoState: UserInfoState,
    val routesListMenu: List<Routes> = emptyList(),
    val lksList: List<LksDto>?
)