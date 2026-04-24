package com.dertefter.settings_account.presentation

sealed class Event {
    object OnNavigateToAuth : Event()

    object OnNavigateBack : Event()

    object OnLogOut : Event()

    object OnRetryCiu : Event()

    object OnRetryYourNeti : Event()

    data class OnChangeAccount(val login: String) : Event()

    data class OnRemoveAccountFromHistory(val login: String) : Event()

}
