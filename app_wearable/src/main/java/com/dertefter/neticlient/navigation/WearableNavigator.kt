package com.dertefter.neticlient.navigation

import com.dertefter.navigation_wearable.NavigationAction
import com.dertefter.navigation_wearable.Navigator
import com.dertefter.navigation_wearable.Routes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WearableNavigator @Inject constructor() : Navigator {

    private val _navigationActions = Channel<NavigationAction>()
    override val navigationActions: Flow<NavigationAction> = _navigationActions.receiveAsFlow()

    override fun navigate(route: Routes) {
        _navigationActions.trySend(NavigationAction.Navigate(route))
    }

    override fun navigateUp() {
        _navigationActions.trySend(NavigationAction.NavigateUp)
    }

    override fun navigateAndClearBackStack(route: Routes, popupTo: Routes, inclusive: Boolean) {
        _navigationActions.trySend(NavigationAction.NavigateAndClearBackStack(route, popupTo, inclusive))
    }
}
