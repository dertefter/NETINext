package com.dertefter.data.datasource.local.room.converter

import androidx.room.TypeConverter
import com.dertefter.data.dto.auth.AuthCreditions
import com.dertefter.data.dto.control_weeks.ControlWeekDto
import com.dertefter.data.dto.docs.DocsItemDto
import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.dto.money.MoneyItemDto
import com.dertefter.data.dto.news.PromoItem
import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.dto.schedule.EventDto
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.dto.user.UserInfoDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromAuthCreditions(value: AuthCreditions?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toAuthCreditions(value: String?): AuthCreditions? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromUserInfoDto(value: UserInfoDto?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toUserInfoDto(value: String?): UserInfoDto? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromContactInfoDto(value: ContactInfoDto?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toContactInfoDto(value: String?): ContactInfoDto? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromGroupDto(value: GroupDto?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toGroupDto(value: String?): GroupDto? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromMessageDto(value: MessageDto?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toMessageDto(value: String?): MessageDto? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromPersonDetailDto(value: PersonDetailDto?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toPersonDetailDto(value: String?): PersonDetailDto? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromTimeSlotDtoList(value: List<TimeSlotDto>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toTimeSlotDtoList(value: String?): List<TimeSlotDto>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromWeekBoundsDtoList(value: List<WeekBoundsDto>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toWeekBoundsDtoList(value: String?): List<WeekBoundsDto>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromGroupDtoList(value: List<GroupDto>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toGroupDtoList(value: String?): List<GroupDto>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromMessageDtoList(value: List<MessageDto>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toMessageDtoList(value: String?): List<MessageDto>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromSessiaResultDtoList(value: List<SessiaResultDto>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toSessiaResultDtoList(value: String?): List<SessiaResultDto>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromPromoItemList(value: List<PromoItem>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toPromoItemList(value: String?): List<PromoItem>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromMoneyItemDtoList(value: List<MoneyItemDto>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toMoneyItemDtoList(value: String?): List<MoneyItemDto>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromEventDtoList(value: List<EventDto>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toEventDtoList(value: String?): List<EventDto>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toStringList(value: String?): List<String>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromLksDtoList(value: List<LksDto>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toLksDtoList(value: String?): List<LksDto>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromDocsItemDtoList(value: List<DocsItemDto>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toDocsItemDtoList(value: String?): List<DocsItemDto>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromControlWeekDtoList(value: List<ControlWeekDto>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toControlWeekDtoList(value: String?): List<ControlWeekDto>? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromControlWeekDto(value: ControlWeekDto?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toControlWeekDto(value: String?): ControlWeekDto? =
        value?.let { json.decodeFromString(it) }
}
