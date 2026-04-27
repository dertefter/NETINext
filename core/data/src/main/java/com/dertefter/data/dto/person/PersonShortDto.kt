package com.dertefter.data.dto.person

import kotlinx.serialization.Serializable

@Serializable
data class PersonShortDto(
    val personId: Long,
    val name: String,
    val avatarUrl: String?
)
