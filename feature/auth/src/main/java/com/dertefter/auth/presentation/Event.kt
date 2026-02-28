package com.dertefter.auth.presentation

sealed class Event {

    object OnSubmit : Event()

    data class OnLoginChanged(val login: String) : Event()

    data class OnPasswordChanged(val password: String) : Event()

}
