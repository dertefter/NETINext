package com.dertefter.calendar.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.schedule.EventDto
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto

data class UiState(
    val group: GroupDto? = null,
    val timeSlots: List<TimeSlotDto> = emptyList(),
    val sessiaTimeSlots: List<TimeSlotDto> = emptyList(),
    val weekBounds: List<WeekBoundsDto> = emptyList(),
    val isUpdating: Boolean = false,
    val isError: AppError? = null,
    val events: List<EventDto> = emptyList()
)
