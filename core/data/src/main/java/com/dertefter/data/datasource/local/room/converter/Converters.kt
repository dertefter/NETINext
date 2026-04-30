package com.dertefter.data.datasource.local.room.converter

import androidx.room.TypeConverter
import com.dertefter.data.dto.auth.AuthCreditions
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromAuthCreditions(value: AuthCreditions?): String? = gson.toJson(value)

    @TypeConverter
    fun toAuthCreditions(value: String?): AuthCreditions? =
        gson.fromJson(value, AuthCreditions::class.java)

    @TypeConverter
    fun fromUserInfoDto(value: UserInfoDto?): String? = gson.toJson(value)

    @TypeConverter
    fun toUserInfoDto(value: String?): UserInfoDto? =
        gson.fromJson(value, UserInfoDto::class.java)

    @TypeConverter
    fun fromContactInfoDto(value: ContactInfoDto?): String? = gson.toJson(value)

    @TypeConverter
    fun toContactInfoDto(value: String?): ContactInfoDto? =
        gson.fromJson(value, ContactInfoDto::class.java)

    @TypeConverter
    fun fromGroupDto(value: GroupDto?): String? = gson.toJson(value)

    @TypeConverter
    fun toGroupDto(value: String?): GroupDto? =
        gson.fromJson(value, GroupDto::class.java)

    @TypeConverter
    fun fromMessageDto(value: MessageDto?): String? = gson.toJson(value)

    @TypeConverter
    fun toMessageDto(value: String?): MessageDto? =
        gson.fromJson(value, MessageDto::class.java)

    @TypeConverter
    fun fromPersonDetailDto(value: PersonDetailDto?): String? = gson.toJson(value)

    @TypeConverter
    fun toPersonDetailDto(value: String?): PersonDetailDto? =
        gson.fromJson(value, PersonDetailDto::class.java)

    @TypeConverter
    fun fromTimeSlotDtoList(value: List<TimeSlotDto>?): String? = gson.toJson(value)

    @TypeConverter
    fun toTimeSlotDtoList(value: String?): List<TimeSlotDto>? {
        val type = object : TypeToken<List<TimeSlotDto>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromWeekBoundsDtoList(value: List<WeekBoundsDto>?): String? = gson.toJson(value)

    @TypeConverter
    fun toWeekBoundsDtoList(value: String?): List<WeekBoundsDto>? {
        val type = object : TypeToken<List<WeekBoundsDto>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromGroupDtoList(value: List<GroupDto>?): String? = gson.toJson(value)

    @TypeConverter
    fun toGroupDtoList(value: String?): List<GroupDto>? {
        val type = object : TypeToken<List<GroupDto>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMessageDtoList(value: List<MessageDto>?): String? = gson.toJson(value)

    @TypeConverter
    fun toMessageDtoList(value: String?): List<MessageDto>? {
        val type = object : TypeToken<List<MessageDto>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromSessiaResultDtoList(value: List<SessiaResultDto>?): String? = gson.toJson(value)

    @TypeConverter
    fun toSessiaResultDtoList(value: String?): List<SessiaResultDto>? {
        val type = object : TypeToken<List<SessiaResultDto>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromPromoItemList(value: List<PromoItem>?): String? = gson.toJson(value)

    @TypeConverter
    fun toPromoItemList(value: String?): List<PromoItem>? {
        val type = object : TypeToken<List<PromoItem>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMoneyItemDtoList(value: List<MoneyItemDto>?): String? = gson.toJson(value)

    @TypeConverter
    fun toMoneyItemDtoList(value: String?): List<MoneyItemDto>? {
        val type = object : TypeToken<List<MoneyItemDto>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromEventDtoList(value: List<EventDto>?): String? = gson.toJson(value)

    @TypeConverter
    fun toEventDtoList(value: String?): List<EventDto>? {
        val type = object : TypeToken<List<EventDto>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromLksDtoList(value: List<LksDto>?): String? = gson.toJson(value)

    @TypeConverter
    fun toLksDtoList(value: String?): List<LksDto>? {
        val type = object : TypeToken<List<LksDto>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromDocsItemDtoList(value: List<DocsItemDto>?): String? = gson.toJson(value)

    @TypeConverter
    fun toDocsItemDtoList(value: String?): List<DocsItemDto>? {
        val type = object : TypeToken<List<DocsItemDto>>() {}.type
        return gson.fromJson(value, type)
    }
}
