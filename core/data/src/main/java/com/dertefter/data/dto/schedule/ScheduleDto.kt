package com.dertefter.data.dto.schedule

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDto (
    val timeSlots: List<TimeSlotDto>,
    val weekBounds: List<WeekBoundsDto>
)