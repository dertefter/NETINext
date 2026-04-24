package com.dertefter.home.presentation

import androidx.paging.PagingData
import com.dertefter.data.dto.news.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class NewsState(
    val newsPagingData: Flow<PagingData<NewsItem>> = emptyFlow(),
)