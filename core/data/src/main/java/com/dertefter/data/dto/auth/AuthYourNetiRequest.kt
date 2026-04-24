package com.dertefter.data.dto.auth

import com.google.gson.annotations.SerializedName

data class AuthYourNetiRequest(
    @SerializedName("X-Username")
    val username: String,

    @SerializedName("X-Password")
    val password: String
)