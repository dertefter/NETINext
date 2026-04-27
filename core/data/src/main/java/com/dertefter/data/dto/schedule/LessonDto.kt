package com.dertefter.data.dto.schedule

import com.dertefter.data.dto.person.PersonShortDto
import kotlinx.serialization.Serializable

@Serializable
data class LessonDto(
    val name: String,
    val type: String?,
    val aud: String?,
    val persons: List<PersonShortDto>?
)
