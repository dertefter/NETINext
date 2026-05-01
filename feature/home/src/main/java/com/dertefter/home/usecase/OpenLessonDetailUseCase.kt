package com.dertefter.home.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class OpenLessonDetailUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke(
        name: String,
        type: String? = null,
        aud: String? = null,
        personIds: List<Long> = emptyList(),
        startTimeString: String,
        endTimeString: String,
        dateString: String
    ) {
        navigator.openAsBottomSheet(
            Routes.LessonDetail(
                name = name,
                type = type,
                aud = aud,
                personIds = personIds,
                startTimeString = startTimeString,
                endTimeString = endTimeString,
                dateString = dateString
            )
        )
    }
}
