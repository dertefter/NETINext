package com.dertefter.person_detail.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class OpenAvatarUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke(url: String) {
        navigator.navigate(Routes.ImageViewer(listOf(url)))
    }
}
