package com.dertefter.calendar.domain.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class OpenGroupSearchUseCase @Inject constructor(

    private val navigator: Navigator,
) {
    operator fun invoke(){
        return navigator.openAsBottomSheet(Routes.SearchGroup)
    }
}