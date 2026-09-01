package com.dertefter.data.dto.user

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoDto(
    @SerializedName("ADDRESS") @SerialName("ADDRESS") val address: String? = null,
    @SerializedName("AVG_BALL") @SerialName("AVG_BALL") val avgBall: Float? = null,
    @SerializedName("BIRTHDAY") @SerialName("BIRTHDAY") val birthday: String? = null,
    @SerializedName("CAN_CHANGE_PHOTO") @SerialName("CAN_CHANGE_PHOTO") val canChangePhoto: Int? = null,
    @SerializedName("CONTROL_WEEK_COUNT") @SerialName("CONTROL_WEEK_COUNT") val controlWeekCount: Int? = null,
    @SerializedName("EMAIL") @SerialName("EMAIL") val email: String? = null,
    @SerializedName("FACEBOOK") @SerialName("FACEBOOK") val facebook: String? = null,
    @SerializedName("FOR_QR") @SerialName("FOR_QR") val forQr: String? = null,
    @SerializedName("ID") @SerialName("ID") val id: Int? = null,
    @SerializedName("ID_CARD") @SerialName("ID_CARD") val idCard: Int? = null,
    @SerializedName("ID_FACULTET") @SerialName("ID_FACULTET") val idFaculty: Int? = null,
    @SerializedName("ID_GROUP") @SerialName("ID_GROUP") val idGroup: Int? = null,
    @SerializedName("ID_PLAN") @SerialName("ID_PLAN") val idPlan: Int? = null,
    @SerializedName("ID_SPECIALIZATION") @SerialName("ID_SPECIALIZATION") val idSpecialization: Int? = null,
    @SerializedName("INSTAGRAM") @SerialName("INSTAGRAM") val instagram: String? = null,
    @SerializedName("IS_CIU") @SerialName("IS_CIU") val isCiu: Int? = null,
    @SerializedName("IS_CURATOR") @SerialName("IS_CURATOR") val isCurator: Int? = null,
    @SerializedName("IS_TUTOR") @SerialName("IS_TUTOR") val isTutor: Int? = null,
    @SerializedName("KEY2") @SerialName("KEY2") val key2: String? = null,
    @SerializedName("LEADER_ID") @SerialName("LEADER_ID") val leaderId: String? = null,
    @SerializedName("LOCKED_BY_FEEDBACK") @SerialName("LOCKED_BY_FEEDBACK") val lockedByFeedback: Int? = null,
    @SerializedName("LOCKED_BY_PHOTO") @SerialName("LOCKED_BY_PHOTO") val lockedByPhoto: Int? = null,
    @SerializedName("LOCKED_FEEDBACK_ID") @SerialName("LOCKED_FEEDBACK_ID") val lockedFeedbackId: Int? = null,
    @SerializedName("MOBILE_PHONE_NUMBER") @SerialName("MOBILE_PHONE_NUMBER") val mobilePhoneNumber: String? = null,
    @SerializedName("NAME") @SerialName("NAME") val name: String? = null,
    @SerializedName("PASS_QR") @SerialName("PASS_QR") val passQr: String? = null,
    @SerializedName("PATRONYMIC") @SerialName("PATRONYMIC") val patronymic: String? = null,
    @SerializedName("PHOTO_PATH") @SerialName("PHOTO_PATH") val photoPath: String? = null,
    @SerializedName("PK") @SerialName("PK") val pk: Int? = null,
    @SerializedName("POST") @SerialName("POST") val post: String? = null,
    @SerializedName("RN") @SerialName("RN") val rn: Int? = null,
    @SerializedName("ROLE") @SerialName("ROLE") val role: Int? = null,
    @SerializedName("SHOW_PASS") @SerialName("SHOW_PASS") val showPass: Int? = null,
    @SerializedName("SNILS") @SerialName("SNILS") val snils: String? = null,
    @SerializedName("SURNAME") @SerialName("SURNAME") val surname: String? = null,
    @SerializedName("SYM_FACULTET") @SerialName("SYM_FACULTET") val symFaculty: String? = null,
    @SerializedName("SYM_GROUP") @SerialName("SYM_GROUP") val symGroup: String? = null,
    @SerializedName("TG") @SerialName("TG") val tg: String? = null,
    @SerializedName("VK") @SerialName("VK") val vk: String? = null,
    @SerializedName("YEAR_ADMISSION") @SerialName("YEAR_ADMISSION") val yearAdmission: Int? = null
) {
    fun toContactInfoDto(): ContactInfoDto {
        return ContactInfoDto(
            name = name,
            surname = surname,
            patronymic = patronymic,
            symGroup = symGroup,
            email = email,
            address = address,
            mobilePhoneNumber = mobilePhoneNumber,
            snils = snils,
            oms = null,
            vk = vk,
            telegram = tg,
            leaderId = leaderId,
        )
    }
}
