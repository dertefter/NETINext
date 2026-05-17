package com.dertefter.control_weeks.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.control_weeks.ControlWeekDto

data class UiState(
    val controlWeeks: List<ControlWeekDto>? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)