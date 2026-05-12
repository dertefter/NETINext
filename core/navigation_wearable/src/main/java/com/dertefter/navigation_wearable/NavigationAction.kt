package com.dertefter.navigation_wearable

sealed interface NavigationAction {
    data class Navigate(val route: Routes) : NavigationAction
    data object NavigateUp : NavigationAction
    data class NavigateAndClearBackStack(val route: Routes, val popupTo: Routes, val inclusive: Boolean = true) : NavigationAction
}
