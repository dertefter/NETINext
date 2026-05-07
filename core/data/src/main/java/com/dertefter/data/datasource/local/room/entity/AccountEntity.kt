package com.dertefter.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dertefter.data.dto.docs.DocsItemDto
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.dto.user.UserInfoDto

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val login: String,
    val userInfo: UserInfoDto? = null,
    val contactInfo: ContactInfoDto? = null,
    val currentGroup: GroupDto? = null,
    val weekBounds: List<WeekBoundsDto>? = null,
    val sessiaResults: List<SessiaResultDto>? = null,
    val groupHistory: List<GroupDto> = emptyList(),
    val shareScoreLink: String? = null,
    val moneyYears: List<String>? = null,
    val lksList: List<LksDto>? = null,
    val docsList: List<DocsItemDto>? = null
)
