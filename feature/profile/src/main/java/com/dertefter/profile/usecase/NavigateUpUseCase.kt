package com.dertefter.profile.usecase

import com.dertefter.navigation.Navigator
import javax.inject.Inject

class NavigateUpUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke() {
        navigator.navigateUp()
    }
}
