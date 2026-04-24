package com.dertefter.home.domain

import android.util.Log
import com.dertefter.data.dto.user.UserInfoDto
import com.dertefter.design.R
import com.dertefter.home.presentation.Event

data class UserInfo(
    val name: String?,
    val surname: String?,
    val patronymic: String?,
    val login: String? = null,
    val email: String?,
    val address: String?,
    val mobilePhoneNumber: String?,
    val symFaculty: String?,
    val symGroup: String?,
    val photoPath: String?,
    val birthday: String?,
    val userChips: List<UserChip> = emptyList(),
)

data class UserChip(
    val name: String,
    val icon: Int,
    val action: Event? = null,
)

fun UserInfoDto.toUserInfo(): UserInfo {
    val userChips = mutableListOf<UserChip>()
    leaderId?.let {
        userChips.add(UserChip("Leader ID", R.drawable.ic_leaderid))
    }


    email?.let {
        userChips.add(UserChip(it, R.drawable.ic_mail_filled))
    }

    mobilePhoneNumber?.let {
        userChips.add(UserChip(it, R.drawable.ic_call_filled))
    }


    tg?.let {
        userChips.add(UserChip("Telegram", R.drawable.ic_telegram_logo))
    }

    snils?.let {
        userChips.add(UserChip("СНИЛС", R.drawable.ic_subtitles_filled))
    }

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
        userChips = userChips,
    )
}
