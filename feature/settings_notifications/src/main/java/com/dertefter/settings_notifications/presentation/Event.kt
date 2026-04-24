package com.dertefter.settings_notifications.presentation

import com.dertefter.data.datasource.remote.PreferredRemoteSource

sealed class Event {
    object OnNavigateBack : Event()

    data class OnChangeNotificationStatus(val isEnabled: Boolean) : Event()


}
