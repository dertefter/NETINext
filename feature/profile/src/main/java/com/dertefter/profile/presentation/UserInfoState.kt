package com.dertefter.profile.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.user.UserInfoDto

data class UserInfoState(
    val userInfo: UserInfoDto? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)