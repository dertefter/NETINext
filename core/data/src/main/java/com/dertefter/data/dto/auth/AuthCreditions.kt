package com.dertefter.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthCreditions(
    val xLogin: String,
    val xPassword: String,
)
