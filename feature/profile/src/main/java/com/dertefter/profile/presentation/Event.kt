package com.dertefter.profile.presentation
import com.dertefter.navigation.Routes

sealed class Event {
    object OnRequestUpdate : Event()
    object OnNavigateBack : Event()

    data class OnNavigateToRoute(val route: Routes) : Event()
}
