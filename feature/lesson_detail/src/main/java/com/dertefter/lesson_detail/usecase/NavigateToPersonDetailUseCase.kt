package com.dertefter.lesson_detail.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class NavigateToPersonDetailUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke(personId: Long) {
        navigator.navigate(Routes.PersonDetail(personId))
    }
}
