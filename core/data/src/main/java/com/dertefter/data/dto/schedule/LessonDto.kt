package com.dertefter.data.dto.schedule

import com.dertefter.data.dto.person.PersonShortDto

data class LessonDto(
    val name: String,
    val type: String?,
    val aud: String?,
    val persons: List<PersonShortDto>?
)
