package com.dertefter.control_weeks.usecase

import com.dertefter.data.dto.control_weeks.ControlWeekDto
import com.dertefter.data.repository.ControlWeeksRepository
import javax.inject.Inject

class UpdateControlWeeksUseCase @Inject constructor(
    private val controlWeeksRepository: ControlWeeksRepository
) {
    suspend operator fun invoke(): Result<List<ControlWeekDto>> {
        return controlWeeksRepository.updateControlWeeks()
    }
}
