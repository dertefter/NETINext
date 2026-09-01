package com.dertefter.data.dto.settings

import kotlinx.serialization.Serializable

@Serializable
data class AppUser(
    val xLogin: String,
    val xPassword: String,
)
