package com.dertefter.person_detail.presentation

sealed class Event {
    object OnUpdatePersonDetail : Event()

    object OnNavigateBack : Event()
}
