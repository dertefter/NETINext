package com.dertefter.news_detail

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.isCut
import com.dertefter.news_detail.presentation.NewsDetailScreen

@Composable
fun NewsDetailRoute(
    newsId: String,
    previewUrl: String?,
    type: String?,
    tags: String?,
    date: String?,
    contentColor: Long?,
    viewModel: NewsDetailViewModel = hiltViewModel(),
) {

    val uiState by viewModel.newsState.collectAsStateWithLifecycle()

    LaunchedEffect(newsId) {
        viewModel.initWith(
            newsId,
        )
    }

    NewsDetailScreen(
        newsState = uiState,
        previewUrl = previewUrl,
        type = type,
        tags = tags,
        date = date,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
        seedColor = contentColor,
    )

}
