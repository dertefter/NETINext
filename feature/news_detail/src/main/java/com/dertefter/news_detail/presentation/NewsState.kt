package com.dertefter.news_detail.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.news.NewsDetailDto
import com.dertefter.data.dto.news.NewsItem

data class NewsState(
    val newsDetailDto: NewsDetailDto? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)