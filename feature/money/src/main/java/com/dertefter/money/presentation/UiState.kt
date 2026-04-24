package com.dertefter.money.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.money.MoneyItemDto

data class UiState(
    val years: List<String> = emptyList(),
    val moneyData: Map<String, List<MoneyItemDto>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: AppError? = null,
)