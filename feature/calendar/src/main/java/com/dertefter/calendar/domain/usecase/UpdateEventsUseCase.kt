package com.dertefter.calendar.domain.usecase

import com.dertefter.data.dto.schedule.EventDto
import com.dertefter.data.repository.ScheduleRepository
import javax.inject.Inject

class UpdateEventsUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(year: String, month: String): Result<List<EventDto>> {
        return scheduleRepository.updateEvents(year, month)
    }
}