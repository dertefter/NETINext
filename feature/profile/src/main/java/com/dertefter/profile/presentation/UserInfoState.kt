package com.dertefter.profile.presentation

import com.dertefter.data.common.AppError
import com.dertefter.profile.domain.UserInfo

data class UserInfoState(
    val userInfo: UserInfo? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)