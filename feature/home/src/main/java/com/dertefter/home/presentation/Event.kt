package com.dertefter.home.presentation

import com.dertefter.data.dto.schedule.GroupDto
import java.time.LocalDate
import java.time.LocalTime

sealed class Event {
    data class  RequestLoadingNews(val fromStart: Boolean = false) : Event()

    data class OnNewsClick(
        val newsId: String,
        val previewUrl: String?,
        val type: String?,
        val tags: String?,
        val date: String?,
        val contentColor: Long?
    ) : Event()

    object OnNavigateToSearchGroup : Event()

    object RequestLoadingPromo : Event()

    object OnNavigateToCalendar : Event()

    data class OnOpenLessonDetail(
        val name: String,
        val type: String? = null,
        val aud: String? = null,
        val personIds: List<Long> = emptyList(),
        val startTime: LocalTime,
        val endTime: LocalTime,
        val date: LocalDate
    ) : Event()

    object OnUpdateSchedule : Event()
}
