package com.dertefter.auth.presentation

sealed class Event {
    object OnSubmit : Event()
    object OnLogout : Event()

    object OnNavigateBack : Event()
    object OnTogglePasswordVisibility : Event()
    data class OnLoginChanged(val login: String) : Event()
    data class OnPasswordChanged(val password: String) : Event()
}
