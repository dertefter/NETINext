package com.dertefter.data.dto.user

data class ContactInfoDto(
    val name: String?,
    val surname: String?,
    val patronymic: String?,
    val symGroup: String?,
    val email: String?,
    val address: String?,
    val mobilePhoneNumber: String?,
    val snils: String?,
    val oms: String?,
    val vk: String?,
    val telegram: String?,
    val leaderId: String?,
    val lksList: List<lkDto> = emptyList(),
)


fun ContactInfoDto.toUserInfoDto(): UserInfoDto {
    return UserInfoDto(
        address = address,
        email = email,
        leaderId = leaderId,
        mobilePhoneNumber = mobilePhoneNumber,
        name = name,
        surname = surname,
        patronymic = patronymic,
        snils = snils,
        symGroup = symGroup,
        tg = telegram,
        vk = vk
    )
}