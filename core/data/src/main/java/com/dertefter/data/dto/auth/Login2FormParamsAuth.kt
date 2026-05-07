package com.dertefter.data.dto.auth

data class Login2FormParamsAuth(
    val sessionCode: String,
    val execution: String,
    val clientId: String,
    val tabId: String,
    val clientData: String,
)