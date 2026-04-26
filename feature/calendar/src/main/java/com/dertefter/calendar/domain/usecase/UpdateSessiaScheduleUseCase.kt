package com.dertefter.calendar.domain.usecase

import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.ScheduleDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.repository.ScheduleRepository
import javax.inject.Inject

class UpdateSessiaScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(group: GroupDto): Result<List<TimeSlotDto>> {
        return scheduleRepository.updateSessiaScheduleForGroup(group)
    }
}