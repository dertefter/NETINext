package com.dertefter.profile.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class NavigateToRouteUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke(route: Routes) {
        if (route == Routes.SwapLks) {
            navigator.openAsBottomSheet(route)
        } else {
            navigator.navigate(route)
        }
    }
}
