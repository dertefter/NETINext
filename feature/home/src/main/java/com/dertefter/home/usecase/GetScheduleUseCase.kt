package com.dertefter.home.usecase

import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(group: GroupDto): Flow<List<TimeSlotDto>?> {
        return scheduleRepository.getSchedule(group)
    }
}
