package com.dertefter.messages.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class NavigateToMessageDetailUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke(idStudent: Long?, idMessage: Long) {
        navigator.navigate(
            Routes.MessagesDetail(
                idStudent = idStudent,
                idMessage = idMessage
            )
        )
    }
}
