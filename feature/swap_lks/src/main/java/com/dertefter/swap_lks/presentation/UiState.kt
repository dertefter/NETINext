package com.dertefter.swap_lks.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.user.LksDto

data class UiState(
    val lksList: List<LksDto>? = null,
    val isLoading: Boolean = false,
    val settingLksId: Int? = null,
    val error: AppError? = null,
)