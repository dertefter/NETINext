package com.dertefter.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class Login2FormParamsAuth(
    val sessionCode: String,
    val execution: String,
    val clientId: String,
    val tabId: String,
    val clientData: String,
)