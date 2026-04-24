package com.dertefter.calendar.presentation

import com.dertefter.data.dto.schedule.TimeSlotDto
import java.time.LocalDate

data class DayState(
    val date: LocalDate,
    val timeSlots: List<TimeSlotDto> = emptyList()
)