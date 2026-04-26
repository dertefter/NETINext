package com.dertefter.calendar.domain.usecase

import com.dertefter.data.dto.schedule.EventDto
import com.dertefter.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(): Flow<List<EventDto>?> {
        return scheduleRepository.getEvents()
    }
}