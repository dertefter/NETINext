package com.dertefter.data.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class LksDto(
    val title: String,
    val subtitle: String?,
    val id: Int?,
    val isSelected: Boolean
)
