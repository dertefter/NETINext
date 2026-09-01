package com.dertefter.data.dto.auth

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthYourNetiRequest(
    @SerializedName("X-Username")
    @SerialName("X-Username")
    val username: String,

    @SerializedName("X-Password")
    @SerialName("X-Password")
    val password: String
)
