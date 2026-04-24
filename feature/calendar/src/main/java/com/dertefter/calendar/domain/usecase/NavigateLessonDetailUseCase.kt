package com.dertefter.calendar.domain.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class NavigateLessonDetailUseCase @Inject constructor(

    private val navigator: Navigator,
) {
    operator fun invoke(
        name: String,
        type: String? = null,
        aud: String? = null,
        personIds: List<Long> = emptyList(),
        startTime: LocalTime,
        endTime: LocalTime,
        date: LocalDate
    ){
        return navigator.openAsBottomSheet(
            Routes.LessonDetail(
                name = name,
                type = type,
                aud = aud,
                personIds = personIds,
                startTimeString = startTime.toString(),
                endTimeString = endTime.toString(),
                dateString = date.toString(),
            )
        )
    }
}