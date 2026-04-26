package com.dertefter.neticlient.presentation

sealed class Event {
    object OnRetryAuthorizeCiu : Event()

    object OnRetryAuthorizeYourNeti : Event()
}
