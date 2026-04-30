package com.dertefter.sessia_results.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class OpenShareScoreUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke() {
        navigator.openAsBottomSheet(Routes.ShareScore)
    }
}
