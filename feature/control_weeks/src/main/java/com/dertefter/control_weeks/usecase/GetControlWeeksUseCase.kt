package com.dertefter.control_weeks.usecase

import com.dertefter.data.dto.control_weeks.ControlWeekDto
import com.dertefter.data.repository.ControlWeeksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetControlWeeksUseCase @Inject constructor(
    private val controlWeeksRepository: ControlWeeksRepository
) {
    operator fun invoke(): Flow<List<ControlWeekDto>> {
        return controlWeeksRepository.getControlWeeks()
    }
}
