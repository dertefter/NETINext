package com.dertefter.calendar.domain.usecase

import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetNextScheduleAfterUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(
        groupDto: GroupDto,
        date: LocalDate,
        time: LocalDate
    ): Flow<List<TimeSlotDto>?> {
        return scheduleRepository.getNextScheduleAfter(groupDto, date, time)
    }
}