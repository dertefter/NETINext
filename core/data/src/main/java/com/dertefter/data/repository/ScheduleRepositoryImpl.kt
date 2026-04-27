package com.dertefter.data.repository

import android.util.Log
import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.schedule.EventDto
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.ScheduleDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : ScheduleRepository {


    override fun getSchedule(groupDto: GroupDto): Flow<List<TimeSlotDto>?> {
        return localDataSource.getTimeSlotsForGroup(groupDto)
    }

    override fun getSessiaSchedule(groupDto: GroupDto): Flow<List<TimeSlotDto>?> {
        return localDataSource.getSessiaSchedule(groupDto)
    }

    override suspend fun updateSessiaScheduleForGroup(group: GroupDto): Result<List<TimeSlotDto>> {
        return remoteDataSource.getSessiaSchedule(group = group).onSuccess { sessiaSchedule ->
            localDataSource.saveSessiaSchedule(group, sessiaSchedule)
        }
    }

    override fun getEvents(): Flow<List<EventDto>?> {
        return localDataSource.getEvents()
    }

    override suspend fun updateEvents(
        year: String,
        month: String
    ): Result<List<EventDto>> {
        return remoteDataSource.getEvents(year, month).onSuccess {
            localDataSource.saveEvents(it)
        }
    }

    override fun getWeeksBounds(): Flow<List<WeekBoundsDto>?> {
        return localDataSource.getWeekBounds()
    }

    override fun getWeekHeader(): Flow<String?> {
        return localDataSource.getWeekHeader()
    }

    override suspend fun updateWeekHeader(): Result<String> {
        return remoteDataSource.getWeekHeader().onSuccess {
            localDataSource.saveWeekHeader(it)
        }
    }

    override fun getNextScheduleAfter(
        groupDto: GroupDto,
        date: LocalDate,
        time: LocalDate
    ): Flow<List<TimeSlotDto>?> {
        return localDataSource.getTimeSlotsForGroup(groupDto).map { timeSlots ->
            timeSlots?.filter { it.getDate().isAfter(date) }
                ?.groupBy { it.getDate() }
                ?.entries
                ?.sortedBy { it.key }
                ?.firstOrNull()
                ?.value
        }
    }

    override suspend fun updateScheduleForGroup(group: GroupDto): Result<ScheduleDto> {
        return remoteDataSource.getSchedule(group = group).onSuccess { schedule ->
            localDataSource.saveTimeSlotsForGroup(group, schedule.timeSlots)
            localDataSource.saveWeekBounds(weekBounds = schedule.weekBounds)
        }.onFailure { e ->
            Log.e("ScheduleRepository", e.stackTraceToString())
        }
    }

}
