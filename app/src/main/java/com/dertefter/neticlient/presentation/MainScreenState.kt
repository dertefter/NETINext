package com.dertefter.neticlient.presentation

import com.dertefter.data.dto.auth.AuthStatus

data class MainScreenState(
    val authStatusCiu: AuthStatus,
    val authStatusYourNeti: AuthStatus,
    val isNotificationEnabled: Boolean?
)
