package com.dertefter.data.dto.messsages

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoveMessageToTrashRequest(
    @SerializedName("id_student") @SerialName("id_student") val idStudent:  Long,
    @SerializedName("id_message") @SerialName("id_message") val idMessage:  Long,
    @SerializedName("is_deleted") @SerialName("is_deleted") val isDeleted: Int = 1
)
