package com.dertefter.navigation

sealed interface NavigationAction {
    data class Navigate(val route: Routes) : NavigationAction
    data object NavigateUp : NavigationAction

    data object HideBottomSheet : NavigationAction

    data class NavigateAndClearBackStack(val route: Routes, val popupTo: Routes, val inclusive: Boolean = true) : NavigationAction
    data class OpenAsBottomSheet(val route: Routes) : NavigationAction
}
