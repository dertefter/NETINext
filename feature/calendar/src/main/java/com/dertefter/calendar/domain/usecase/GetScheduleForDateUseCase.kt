package com.dertefter.calendar.domain.usecase

import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetScheduleForDateUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(groupDto: GroupDto, date: LocalDate): Flow<List<TimeSlotDto>?> {
        return scheduleRepository.getScheduleForDate(groupDto, date)
    }
}