package com.dertefter.control_weeks.usecase

import com.dertefter.navigation.Navigator
import javax.inject.Inject

class NavigateBackUseCase @Inject constructor(

    private val navigator: Navigator,
) {
    operator fun invoke(){
        return navigator.navigateUp()
    }
}