package com.dertefter.home.presentation

import com.dertefter.data.common.AppError
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import java.time.LocalDate

data class ScheduleState(
    val date: LocalDate? = null,
    val group: GroupDto? = null,
    val timeSlots: List<TimeSlotDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: AppError? = null,
    val isTgShow: Boolean = false
)