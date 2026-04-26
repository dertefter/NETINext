package com.dertefter.data.dto.schedule

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class EventDto(
    val title: String,
    val url: String?,
    val dateString: String
) {
    fun getDate(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return LocalDate.parse(dateString, formatter)
    }
}
