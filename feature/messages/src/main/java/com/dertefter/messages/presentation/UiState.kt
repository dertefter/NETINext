package com.dertefter.messages.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.dto.news.NewsItem

data class UiState(
    val messages: List<MessageDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: AppError? = null,
    val isWarning: Boolean = false,
    val filterModes: List<FilterMode> = emptyList(),
    val filterMode: FilterMode = FilterMode.ALL,
)