package com.dertefter.search_person.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.person.PersonShortDto
import com.dertefter.data.dto.schedule.GroupDto

data class UiState(
    val query: String,
    val persons: List<PersonShortDto>,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)