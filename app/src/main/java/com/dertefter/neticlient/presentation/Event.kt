package com.dertefter.neticlient.presentation

sealed class Event {
    object OnRetryAuthorize : Event()
}
