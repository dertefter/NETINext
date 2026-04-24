package com.dertefter.news_detail.presentation

sealed class Event {
    object RequestLoadingNewsDetail : Event()

    object OnNavigateBack : Event()

    data class OnNavigateToImageViewer(
        val imageUrls: List<String>,
        val viewPosition: Int? = null,
    ) : Event()
}
