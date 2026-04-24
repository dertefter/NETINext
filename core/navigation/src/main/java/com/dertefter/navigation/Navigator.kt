package com.dertefter.navigation

import kotlinx.coroutines.flow.Flow

interface Navigator {

    val navigationActions: Flow<NavigationAction>

    fun navigate(route: Routes)

    fun openAsBottomSheet(route: Routes)

    fun hideBottomSheet()

    fun navigateUp()

    fun navigateAndClearBackStack(route: Routes, popupTo: Routes, inclusive: Boolean = true)

}