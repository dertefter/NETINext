package com.dertefter.data.dto.messsages

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class MessageDto(
    @SerializedName("DATE_READ") val dateRead: String? = null,

    @SerializedName("DATE_SENT") val dateSent: String?,

    @SerializedName("FIO_AUTHOR") val fioAuthor: String,

    @SerializedName("ID") val id: Long,

    @SerializedName("IDCATEGORY") val idCategory: Int? = null,

    @SerializedName("ID_AUTHOR") val idAuthor: Long? = null,

    @SerializedName("ID_STUDENT") val idStudent: Long? = null,

    @SerializedName("IS_DELETED") val isDeleted: Int, @SerializedName("IS_READ") val isRead: Int,

    @SerializedName("MESSAGE_URL") val messageUrl: String? = null,

    @SerializedName("PORTRAIT_URL") val portraitUrl: String? = null,

    @SerializedName("SENDER_TYPE") val senderType: Int = 1000,

    @SerializedName("SNAME") val sname: String? = null,

    @SerializedName("TEXT") val text: String,

    @SerializedName("TITTLE") val title: String,

    @SerializedName("WITH_BLOCK") val withBlock: Int? = null,

    @SerializedName("WITH_POPUP") val withPopup: Int? = null
) {
    fun getLocalDateTime(): LocalDateTime {
        val dateString = dateSent ?: return LocalDateTime.now()

        return try {
            LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (_: Exception) {
            try {
                LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yy"))
                    .atStartOfDay()
            } catch (_: Exception) {
                LocalDateTime.of(1970, 1, 1, 0, 0)
            }
        }
    }
}