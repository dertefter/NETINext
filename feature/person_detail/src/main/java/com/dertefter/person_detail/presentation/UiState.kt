package com.dertefter.person_detail.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.person.PersonDetailDto

data class UiState(
    val personDetail: PersonDetailDto? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)