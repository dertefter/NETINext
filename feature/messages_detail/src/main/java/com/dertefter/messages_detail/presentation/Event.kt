package com.dertefter.messages_detail.presentation

sealed class Event {
    object OnNavigateBack : Event()

    data class OnMoveToArchive(val isArchived: Boolean) : Event()

    object OnDelete : Event()

    object OnUpdateMessage : Event()
}
