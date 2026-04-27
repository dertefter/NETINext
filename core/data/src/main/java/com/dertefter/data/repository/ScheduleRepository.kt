package com.dertefter.data.repository

import com.dertefter.data.dto.schedule.EventDto
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.LessonDto
import com.dertefter.data.dto.schedule.ScheduleDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import com.dertefter.data.dto.user.UserInfoDto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ScheduleRepository {

    fun getSchedule(groupDto: GroupDto): Flow<List<TimeSlotDto>?>

    fun getSessiaSchedule(groupDto: GroupDto): Flow<List<TimeSlotDto>?>

    suspend fun updateSessiaScheduleForGroup(group: GroupDto): Result<List<TimeSlotDto>>

    fun getEvents(): Flow<List<EventDto>?>

    suspend fun updateEvents(year: String, month: String): Result<List<EventDto>>

    fun getWeeksBounds(): Flow<List<WeekBoundsDto>?>

    fun getWeekHeader(): Flow<String?>

    suspend fun updateWeekHeader(): Result<String>

    fun getNextScheduleAfter(
        groupDto: GroupDto,
        date: LocalDate,
        time: LocalDate
    ): Flow<List<TimeSlotDto>?>

    suspend fun updateScheduleForGroup(group: GroupDto): Result<ScheduleDto>

}