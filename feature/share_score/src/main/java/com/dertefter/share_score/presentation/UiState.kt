package com.dertefter.share_score.presentation

import com.dertefter.data.common.AppError

data class UiState(
    val shareScoreLink: String? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)