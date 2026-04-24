package com.dertefter.data.dto.person



data class PersonDetailDto(
    val personId: Long,
    val name: String?,
    val avatarUrl: String? = null,
    val post: String? = null,
    val contactInfo: List<String> = emptyList(),
    val profiles: List<String> = emptyList(),
    val disciplines: List<String> = emptyList(),
    val hasTimetable: Boolean = false
)
