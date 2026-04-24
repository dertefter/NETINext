package com.dertefter.data.repository

import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.LessonDto
import com.dertefter.data.dto.schedule.ScheduleDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import com.dertefter.data.dto.user.UserInfoDto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ScheduleRepository {

    fun getScheduleForDate(groupDto: GroupDto, date: LocalDate): Flow<List<TimeSlotDto>?>

    fun getSchedule(groupDto: GroupDto): Flow<List<TimeSlotDto>?>

    fun getWeeksBounds(): Flow<List<WeekBoundsDto>?>

    fun getNextScheduleAfter(
        groupDto: GroupDto,
        date: LocalDate,
        time: LocalDate
    ): Flow<List<TimeSlotDto>?>

    suspend fun updateScheduleForGroup(group: GroupDto): Result<ScheduleDto>

}