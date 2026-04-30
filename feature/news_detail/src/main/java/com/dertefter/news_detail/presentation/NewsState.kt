package com.dertefter.news_detail.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.news.NewsDetailDto

data class NewsState(
    val newsDetailDto: NewsDetailDto? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)