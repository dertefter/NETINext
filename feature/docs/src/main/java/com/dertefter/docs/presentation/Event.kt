package com.dertefter.docs.presentation

import com.dertefter.data.dto.docs.DocsItemDto

sealed class Event {
    object OnUpdate : Event()

    data class OnOpenDocDetail(val docItem: DocsItemDto) : Event()

    object OnNavigateToNewDocument : Event()
    object OnNavigateBack : Event()
}
