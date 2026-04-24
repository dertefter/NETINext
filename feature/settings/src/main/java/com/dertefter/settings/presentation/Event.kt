package com.dertefter.settings.presentation

import com.dertefter.navigation.Routes

sealed class Event {

    data class OnNavigateTo(val route: Routes) : Event()
    data object OnNavigateBack : Event()


}
