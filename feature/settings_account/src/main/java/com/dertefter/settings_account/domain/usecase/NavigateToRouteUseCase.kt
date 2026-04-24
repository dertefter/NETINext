package com.dertefter.settings_account.domain.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class NavigateToRouteUseCase @Inject constructor(

    private val navigator: Navigator,
) {
    operator fun invoke(route: Routes){
        return navigator.navigate(route)
    }
}