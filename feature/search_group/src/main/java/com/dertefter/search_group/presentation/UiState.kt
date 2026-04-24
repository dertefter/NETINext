package com.dertefter.search_group.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.schedule.GroupDto

data class UiState(
    val query: String,
    val groups: List<GroupDto>,
    val groupHistory: List<GroupDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: AppError? = null,
)