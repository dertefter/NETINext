package com.dertefter.data.dto.user

import com.google.gson.annotations.SerializedName

data class UserInfoDto(
    @SerializedName("ADDRESS") val address: String? = null,
    @SerializedName("AVG_BALL") val avgBall: Float? = null,
    @SerializedName("BIRTHDAY") val birthday: String? = null,
    @SerializedName("CAN_CHANGE_PHOTO") val canChangePhoto: Int? = null,
    @SerializedName("CONTROL_WEEK_COUNT") val controlWeekCount: Int? = null,
    @SerializedName("EMAIL") val email: String? = null,
    @SerializedName("FACEBOOK") val facebook: String? = null,
    @SerializedName("FOR_QR") val forQr: String? = null,
    @SerializedName("ID") val id: Int? = null,
    @SerializedName("ID_CARD") val idCard: Int? = null,
    @SerializedName("ID_FACULTET") val idFaculty: Int? = null,
    @SerializedName("ID_GROUP") val idGroup: Int? = null,
    @SerializedName("ID_PLAN") val idPlan: Int? = null,
    @SerializedName("ID_SPECIALIZATION") val idSpecialization: Int? = null,
    @SerializedName("INSTAGRAM") val instagram: String? = null,
    @SerializedName("IS_CIU") val isCiu: Int? = null,
    @SerializedName("IS_CURATOR") val isCurator: Int? = null,
    @SerializedName("IS_TUTOR") val isTutor: Int? = null,
    @SerializedName("KEY2") val key2: String? = null,
    @SerializedName("LEADER_ID") val leaderId: String? = null,
    @SerializedName("LOCKED_BY_FEEDBACK") val lockedByFeedback: Int? = null,
    @SerializedName("LOCKED_BY_PHOTO") val lockedByPhoto: Int? = null,
    @SerializedName("LOCKED_FEEDBACK_ID") val lockedFeedbackId: Int? = null,
    @SerializedName("MOBILE_PHONE_NUMBER") val mobilePhoneNumber: String? = null,
    @SerializedName("NAME") val name: String? = null,
    @SerializedName("PASS_QR") val passQr: String? = null,
    @SerializedName("PATRONYMIC") val patronymic: String? = null,
    @SerializedName("PHOTO_PATH") val photoPath: String? = null,
    @SerializedName("PK") val pk: Int? = null,
    @SerializedName("POST") val post: String? = null,
    @SerializedName("RN") val rn: Int? = null,
    @SerializedName("ROLE") val role: Int? = null,
    @SerializedName("SHOW_PASS") val showPass: Int? = null,
    @SerializedName("SNILS") val snils: String? = null,
    @SerializedName("SURNAME") val surname: String? = null,
    @SerializedName("SYM_FACULTET") val symFaculty: String? = null,
    @SerializedName("SYM_GROUP") val symGroup: String? = null,
    @SerializedName("TG") val tg: String? = null,
    @SerializedName("VK") val vk: String? = null,
    @SerializedName("YEAR_ADMISSION") val yearAdmission: Int? = null
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