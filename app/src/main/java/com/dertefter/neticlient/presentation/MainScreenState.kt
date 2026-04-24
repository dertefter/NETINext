package com.dertefter.neticlient.presentation

import com.dertefter.data.dto.auth.AuthStatus

data class MainScreenState(
    val authStatus: AuthStatus,
    val authStatusNotify: AuthStatus?,
    val themeColor: Long?,
    val isShapeCut: Boolean?,
    val isNotificationEnabled: Boolean?
)
