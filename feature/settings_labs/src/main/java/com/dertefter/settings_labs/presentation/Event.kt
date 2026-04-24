package com.dertefter.settings_labs.presentation

import com.dertefter.data.datasource.remote.PreferredRemoteSource

sealed class Event {
    object OnNavigateBack : Event()

    data class OnSelectPreferredDataSource(val p: PreferredRemoteSource) : Event()


}
