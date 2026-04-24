package com.dertefter.data.dto.messsages

import com.google.gson.annotations.SerializedName

data class ReadMessageRequest(
    @SerializedName("id_student") val idStudent:  Long,
    @SerializedName("id_message") val idMessage:  Long,
    @SerializedName("is_read") val isRead: Int = 1
)