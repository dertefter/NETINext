package com.dertefter.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dertefter.data.dto.auth.AuthCreditions
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.UserInfoDto

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val login: String,
    val authCreds: AuthCreditions? = null,
    val userInfo: UserInfoDto? = null,
    val contactInfo: ContactInfoDto? = null,
    val currentGroup: GroupDto? = null
)
