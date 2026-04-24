package com.dertefter.contact_info.domain

import com.dertefter.data.dto.user.UserInfoDto

data class UserInfo(
    val name: String?,
    val surname: String?,
    val patronymic: String?,
    val email: String?,
    val address: String?,
    val mobilePhoneNumber: String?,
    val symFaculty: String?,
    val symGroup: String?,
    val photoPath: String?,
    val birthday: String?,
)


fun UserInfoDto.toUserInfo(): UserInfo {

    return UserInfo(
        name = name,
        surname = surname,
        patronymic = patronymic,
        email = email,
        address = address,
        mobilePhoneNumber = mobilePhoneNumber,
        symFaculty = symFaculty,
        symGroup = symGroup,
        photoPath = photoPath,
        birthday = birthday,
    )
}
