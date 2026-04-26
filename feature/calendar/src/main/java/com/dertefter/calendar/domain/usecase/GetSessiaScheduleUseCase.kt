package com.dertefter.calendar.domain.usecase

import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessiaScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(groupDto: GroupDto): Flow<List<TimeSlotDto>?> {
        return scheduleRepository.getSessiaSchedule(groupDto)
    }
}