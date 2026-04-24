package com.dertefter.calendar.domain.usecase

import com.dertefter.data.dto.schedule.WeekBoundsDto
import com.dertefter.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeeksBoundsUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(): Flow<List<WeekBoundsDto>?> {
        return scheduleRepository.getWeeksBounds()
    }
}