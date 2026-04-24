package com.dertefter.share_score.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.dto.sessia_results.SessiaResultDto

data class UiState(
    val shareScoreLink: String? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)