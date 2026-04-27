package com.dertefter.data.dto.schedule

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class TimeSlotDto(
    val dateString: String,
    val startTimeString: String,
    val endTimeString: String,
    val lessons: List<LessonDto>
) {
    fun getStartTime(): LocalTime {
        return LocalTime.parse(startTimeString)
    }

    fun getEndTime(): LocalTime {
        return LocalTime.parse(endTimeString)
    }

    fun getDate(): LocalDate {
        return LocalDate.parse(dateString)
    }

}
