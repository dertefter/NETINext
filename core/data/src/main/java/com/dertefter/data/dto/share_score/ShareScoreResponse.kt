package com.dertefter.data.dto.share_score

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShareScoreResponse(
    @SerializedName("access_url")
    @SerialName("access_url")
    val accessUrl: String
)
