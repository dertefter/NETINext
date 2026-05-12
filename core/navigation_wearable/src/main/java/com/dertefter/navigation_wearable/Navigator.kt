package com.dertefter.navigation_wearable

import kotlinx.coroutines.flow.Flow

interface Navigator {

    val navigationActions: Flow<NavigationAction>

    fun navigate(route: Routes)


    fun navigateUp()

    fun navigateAndClearBackStack(route: Routes, popupTo: Routes, inclusive: Boolean = true)

}