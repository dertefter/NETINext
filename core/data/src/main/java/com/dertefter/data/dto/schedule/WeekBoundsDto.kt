package com.dertefter.data.dto.schedule

import java.time.LocalDate

data class WeekBoundsDto(
    val startDateString: String,
    val weekNumber: Int,
){
    fun getStartDate(): LocalDate {
        return LocalDate.parse(startDateString)
    }

    fun getEndDate(): LocalDate {
        return getStartDate().plusDays(6)
    }

}
