package com.dertefter.calendar.presentation

import com.dertefter.calendar.presentation.componets.calendar.CalendarState
import com.dertefter.data.dto.schedule.GroupDto
import java.time.LocalDate
import java.time.LocalTime

sealed class Event {

    object OnOpenGroupSearch : Event()

    object OnUpdateSchedule : Event()

    object OnNavigateBack : Event()

    data class OnOpenLessonDetail(
        val name: String,
        val type: String? = null,
        val aud: String? = null,
        val personIds: List<Long> = emptyList(),
        val startTime: LocalTime,
        val endTime: LocalTime,
        val date: LocalDate
    ) : Event()


}
