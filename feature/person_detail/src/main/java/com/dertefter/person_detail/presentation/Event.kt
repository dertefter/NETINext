package com.dertefter.person_detail.presentation

sealed class Event {
    object OnUpdatePersonDetail : Event()

    data class OnOpenAvatar(val url: String) : Event()

    object OnNavigateBack : Event()
}
