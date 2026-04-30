package com.dertefter.settings_notifications.presentation

sealed class Event {
    object OnNavigateBack : Event()

    data class OnChangeNotificationStatus(val isEnabled: Boolean) : Event()


}
