package com.dertefter.data.dto.schedule

data class ScheduleDto (
    val timeSlots: List<TimeSlotDto>,
    val weekBounds: List<WeekBoundsDto>
)