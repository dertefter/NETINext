package com.dertefter.docs.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.docs.DocsItemDto

data class UiState(
    val docsList: List<DocsItemDto> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)