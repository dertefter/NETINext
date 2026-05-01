package com.dertefter.home.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class OpenSearchGroupUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke() {
        navigator.openAsBottomSheet(Routes.SearchGroup)
    }
}
