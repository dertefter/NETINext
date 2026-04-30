package com.dertefter.sessia_results.usecase

import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.repository.SessiaResultsRepository
import javax.inject.Inject

class UpdateSessiaResultsUseCase @Inject constructor(
    private val sessiaResultsRepository: SessiaResultsRepository
) {
    suspend operator fun invoke(): Result<List<SessiaResultDto>> {
        return sessiaResultsRepository.updateSessiaResults()
    }
}
